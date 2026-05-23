package com.vietnam.pji.services.impl;

import com.vietnam.pji.constant.PendingLabTaskStatus;
import com.vietnam.pji.exception.ResourceNotFoundException;
import com.vietnam.pji.model.agentic.PendingLabTask;
import com.vietnam.pji.model.medical.LabResult;
import com.vietnam.pji.model.medical.PjiEpisode;
import com.vietnam.pji.repository.EpisodeRepository;
import com.vietnam.pji.repository.LabResultRepository;
import com.vietnam.pji.repository.PendingLabTaskRepository;
import com.vietnam.pji.services.PendingLabTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PendingLabTaskServiceImpl implements PendingLabTaskService {

    private final PendingLabTaskRepository pendingLabTaskRepository;
    private final LabResultRepository labResultRepository;
    private final EpisodeRepository episodeRepository;

    /**
     * Maps completeness field keys from Python's completeness.py to the JSONB
     * storage location and canonical frontend identifier inside LabResult.
     * Sections: "hematology" (hematologyTests), "fluid" (fluidAnalysis),
     * "biochemical" (biochemicalData).
     * The id matches the canonical frontend row when one exists (e.g. ht_17 for
     * D-dimer); otherwise it uses a {section}_extra_{slug} prefix so the row
     * still merges into the clinician view as an AI-requested row.
     */
    private record LabFieldSpec(String section, String id, String name,
                                String normalRange, String defaultUnit) {}

    private static final Map<String, LabFieldSpec> FIELD_TO_LAB_MAPPING = Map.ofEntries(
            Map.entry("serum_CRP",
                    new LabFieldSpec("hematology", "ht_extra_crp", "CRP", "< 5", "mg/L")),
            Map.entry("serum_ESR",
                    new LabFieldSpec("hematology", "ht_7", "Máu lắng", "< 10", "mm")),
            Map.entry("serum_D_Dimer",
                    new LabFieldSpec("hematology", "ht_17", "D-dimer", "< 0.5", "mg/L FEU")),
            Map.entry("serum_IL6",
                    new LabFieldSpec("hematology", "ht_18", "Serum IL-6", "< 7.0", "pg/mL")),
            Map.entry("synovial_WBC",
                    new LabFieldSpec("fluid", "fa_3", "Bạch cầu (Dịch)", "", "Tế bào/Vi trường")),
            Map.entry("synovial_PMN",
                    new LabFieldSpec("fluid", "fa_6", "%PMN (Dịch)", "", "%")),
            Map.entry("synovial_alpha_defensin",
                    new LabFieldSpec("fluid", "fa_extra_alpha_defensin",
                            "Alpha Defensin (dịch)", "< 0.12", "ug/mL")),
            Map.entry("synovial_LE",
                    new LabFieldSpec("fluid", "fa_extra_leukocyte_esterase",
                            "Leukocyte Esterase (dịch)", "10 - 25", "LEU/µL")),
            Map.entry("renal_function",
                    new LabFieldSpec("biochemical", "bc_6", "Creatinine", "59 - 104", "µmol/l")),
            Map.entry("liver_function",
                    new LabFieldSpec("biochemical", "bc_9", "ALT", "35 - 52", "U/L"))
    );

    /**
     * Build a self-describing spec for a field the AI proposed but isn't in the
     * static mapping above. Defaults to the hematology section since that's the
     * most common shape for serum biomarkers; clinician can move the row if needed.
     */
    private static LabFieldSpec fallbackSpec(String field) {
        String slug = field == null ? "unknown"
                : field.toLowerCase().replaceAll("[^a-z0-9]+", "_")
                        .replaceAll("(^_+|_+$)", "");
        if (slug.isEmpty()) slug = "unknown";
        return new LabFieldSpec("hematology", "ht_extra_" + slug, field, "", "");
    }

    @Override
    @Transactional(readOnly = true)
    public List<PendingLabTask> getMyPendingTasks(Long userId) {
        List<PendingLabTask> tasks = pendingLabTaskRepository
                .findByAssignedToUserIdAndStatusOrderByCreatedAtDesc(userId, PendingLabTaskStatus.PENDING);
        tasks.forEach(t -> {
            Hibernate.initialize(t.getEpisode());
            if (t.getEpisode() != null) {
                Hibernate.initialize(t.getEpisode().getPatient());
            }
            Hibernate.initialize(t.getPatient());
            if (t.getFulfilledLabResult() != null) {
                Hibernate.initialize(t.getFulfilledLabResult());
                Hibernate.initialize(t.getFulfilledLabResult().getEpisode());
                if (t.getFulfilledLabResult().getEpisode() != null) {
                    Hibernate.initialize(t.getFulfilledLabResult().getEpisode().getPatient());
                }
            }
        });
        return tasks;
    }

    @Override
    @Transactional(readOnly = true)
    public long getMyPendingCount(Long userId) {
        return pendingLabTaskRepository.countByAssignedToUserIdAndStatus(userId, PendingLabTaskStatus.PENDING);
    }

    @Override
    @Transactional
    public void dismiss(Long taskId) {
        PendingLabTask task = pendingLabTaskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Pending task not found"));
        task.setStatus(PendingLabTaskStatus.DISMISSED);
        pendingLabTaskRepository.save(task);
    }

    @Override
    @Transactional
    public void fulfillByQuickEntry(Long taskId, Object value, String unit) {
        PendingLabTask task = pendingLabTaskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Pending task not found"));

        if (task.getStatus() != PendingLabTaskStatus.PENDING) {
            return;
        }

        Long episodeId = task.getEpisode().getId();
        String field = task.getField();
        LabFieldSpec spec = FIELD_TO_LAB_MAPPING.getOrDefault(field, fallbackSpec(field));

        // Get or create the latest lab result for this episode
        LabResult labResult = getOrCreateLatestLabResult(episodeId);

        // Self-describing row: id + name + normalRange let the frontend merge into
        // the canonical default row (when one exists) or render the AI-proposed row
        // via the ht_extra_/fa_extra_ namespace without crashing.
        Map<String, Object> testEntry = new LinkedHashMap<>();
        testEntry.put("id", spec.id());
        testEntry.put("name", spec.name());
        testEntry.put("value", value);
        testEntry.put("unit", unit != null ? unit : spec.defaultUnit());
        testEntry.put("normalRange", spec.normalRange());

        switch (spec.section()) {
            case "hematology" -> {
                List<Map<String, Object>> tests = labResult.getHematologyTests();
                if (tests == null) tests = new ArrayList<>();
                removeExisting(tests, spec.id(), spec.name());
                tests.add(testEntry);
                labResult.setHematologyTests(tests);
            }
            case "fluid" -> {
                List<Map<String, Object>> tests = labResult.getFluidAnalysis();
                if (tests == null) tests = new ArrayList<>();
                removeExisting(tests, spec.id(), spec.name());
                tests.add(testEntry);
                labResult.setFluidAnalysis(tests);
            }
            case "biochemical" -> {
                Map<String, Object> data = labResult.getBiochemicalData();
                if (data == null) data = new LinkedHashMap<>();
                data.put(spec.name(), testEntry);
                labResult.setBiochemicalData(data);
            }
        }

        LabResult saved = labResultRepository.save(labResult);

        // Mark task as fulfilled
        task.setStatus(PendingLabTaskStatus.FULFILLED);
        task.setFulfilledLabResult(saved);
        pendingLabTaskRepository.save(task);

        log.info("Quick-entry fulfilled: taskId={}, field={}, episodeId={}", taskId, field, episodeId);
    }

    @Override
    @Transactional
    public void createFromCompleteness(Long episodeId, Long patientId, Long userId,
                                       Long runId, List<Map<String, Object>> missingItems) {
        if (missingItems == null || missingItems.isEmpty()) return;

        PjiEpisode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new ResourceNotFoundException("Episode not found"));

        for (Map<String, Object> item : missingItems) {
            String field = (String) item.get("field");
            String category = (String) item.get("category");
            String importance = (String) item.get("importance");
            String message = (String) item.get("message");

            Optional<PendingLabTask> existing = pendingLabTaskRepository
                    .findByEpisodeIdAndFieldAndStatus(episodeId, field, PendingLabTaskStatus.PENDING);
            if (existing.isPresent()) {
                if (userId != null) {
                    PendingLabTask task = existing.get();
                    // Async worker can create unassigned tasks before the user clicks save.
                    // Claim those tasks so they show up in the current user's drawer.
                    if (task.getAssignedToUserId() == null) {
                        task.setAssignedToUserId(userId);
                    }
                    if (task.getCreatedFromRunId() == null) {
                        task.setCreatedFromRunId(runId);
                    }
                    pendingLabTaskRepository.save(task);
                }
                continue;
            }

            PendingLabTask task = PendingLabTask.builder()
                    .episode(episode)
                    .patient(episode.getPatient())
                    .assignedToUserId(userId)
                    .field(field)
                    .category(category)
                    .importance(importance)
                    .message(message)
                    .status(PendingLabTaskStatus.PENDING)
                    .createdFromRunId(runId)
                    .build();

            pendingLabTaskRepository.save(task);
        }

        log.info("Created pending tasks from completeness: episodeId={}, runId={}, count={}",
                episodeId, runId, missingItems.size());
    }

    @Override
    @Transactional
    public void autoFulfillForEpisode(Long episodeId, Long labResultId,
                                      List<Map<String, Object>> hematologyTests,
                                      List<Map<String, Object>> fluidAnalysis,
                                      Map<String, Object> biochemicalData) {
        List<PendingLabTask> pendingTasks = pendingLabTaskRepository
                .findByEpisodeIdAndStatus(episodeId, PendingLabTaskStatus.PENDING);

        if (pendingTasks.isEmpty()) return;

        Set<String> presentFields = extractPresentFields(hematologyTests, fluidAnalysis, biochemicalData);

        LabResult labResult = labResultRepository.findById(labResultId).orElse(null);

        for (PendingLabTask task : pendingTasks) {
            LabFieldSpec spec = FIELD_TO_LAB_MAPPING.get(task.getField());
            if (spec != null && presentFields.contains(spec.name())) {
                task.setStatus(PendingLabTaskStatus.FULFILLED);
                task.setFulfilledLabResult(labResult);
                pendingLabTaskRepository.save(task);
                log.info("Auto-fulfilled pending task: id={}, field={}", task.getId(), task.getField());
            }
        }
    }

    private LabResult getOrCreateLatestLabResult(Long episodeId) {
        var page = labResultRepository.findByEpisodeId(
                episodeId, PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "createdAt")));

        if (!page.isEmpty()) {
            return page.getContent().get(0);
        }

        PjiEpisode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new ResourceNotFoundException("Episode not found"));
        LabResult newLab = LabResult.builder()
                .episode(episode)
                .hematologyTests(new ArrayList<>())
                .fluidAnalysis(new ArrayList<>())
                .biochemicalData(new LinkedHashMap<>())
                .build();
        return labResultRepository.save(newLab);
    }

    private Set<String> extractPresentFields(List<Map<String, Object>> hematologyTests,
                                             List<Map<String, Object>> fluidAnalysis,
                                             Map<String, Object> biochemicalData) {
        Set<String> fields = new HashSet<>();
        if (hematologyTests != null) {
            for (Map<String, Object> test : hematologyTests) {
                Object name = test.get("name");
                if (name != null && test.get("value") != null) {
                    fields.add(name.toString());
                }
            }
        }
        if (fluidAnalysis != null) {
            for (Map<String, Object> test : fluidAnalysis) {
                Object name = test.get("name");
                if (name != null && test.get("value") != null) {
                    fields.add(name.toString());
                }
            }
        }
        if (biochemicalData != null) {
            for (Map.Entry<String, Object> entry : biochemicalData.entrySet()) {
                if (entry.getValue() != null) {
                    fields.add(entry.getKey());
                }
            }
        }
        return fields;
    }

    private void removeExisting(List<Map<String, Object>> tests, String id, String name) {
        tests.removeIf(t -> (id != null && id.equals(t.get("id")))
                || (name != null && name.equals(t.get("name"))));
    }
}
