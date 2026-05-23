package com.vietnam.pji.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vietnam.pji.constant.ReviewStatus;
import com.vietnam.pji.dto.request.PriorAcceptedDiagnosisDTO;
import com.vietnam.pji.exception.ResourceNotFoundException;
import com.vietnam.pji.model.agentic.AiRecommendationItem;
import com.vietnam.pji.model.agentic.AiRecommendationRun;
import com.vietnam.pji.model.agentic.DoctorRecommendationReview;
import com.vietnam.pji.model.medical.PjiEpisode;
import com.vietnam.pji.repository.AiRecommendationItemRepository;
import com.vietnam.pji.repository.DoctorRecommendationReviewRepository;
import com.vietnam.pji.repository.EpisodeRepository;
import com.vietnam.pji.services.PriorAcceptedDiagnosisAssemblerService;
import com.vietnam.pji.utils.DoctorModificationUtils;
import com.vietnam.pji.utils.JsonUtils;
import com.vietnam.pji.utils.mapper.PriorAcceptedDiagnosisMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Builds the "prior physician-validated diagnoses" context that the backend
 * carries to the RAG service alongside the current clinical snapshot.
 *
 * <p>Bounded by {@link #MAX_PRIOR_EPISODES} most-recent episodes and
 * {@link #LOOKBACK_MONTHS} months so prompt size stays predictable.</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PriorAcceptedDiagnosisAssemblerServiceImpl implements PriorAcceptedDiagnosisAssemblerService {

    private static final int MAX_PRIOR_EPISODES = 3;
    private static final int LOOKBACK_MONTHS = 24;

    private static final List<ReviewStatus> ACCEPTED_STATUSES =
            List.of(ReviewStatus.ACCEPTED, ReviewStatus.MODIFIED);

    private final EpisodeRepository episodeRepository;
    private final DoctorRecommendationReviewRepository reviewRepository;
    private final AiRecommendationItemRepository itemRepository;
    private final PriorAcceptedDiagnosisMapper mapper;
    private final ObjectMapper objectMapper;

    @Override
    public List<PriorAcceptedDiagnosisDTO> assemble(Long currentEpisodeId) {
        PjiEpisode current = episodeRepository.findById(currentEpisodeId)
                .orElseThrow(() -> new ResourceNotFoundException("Episode not found: " + currentEpisodeId));

        if (current.getPatient() == null || current.getPatient().getId() == null) {
            return List.of();
        }

        Long patientId = current.getPatient().getId();
        Date sinceCutoff = Date.from(Instant.now().minus(LOOKBACK_MONTHS * 30L, ChronoUnit.DAYS));

        List<DoctorRecommendationReview> reviews = reviewRepository
                .findAcceptedReviewsForPatientExcludingEpisode(
                        patientId,
                        currentEpisodeId,
                        ACCEPTED_STATUSES,
                        sinceCutoff,
                        PageRequest.of(0, MAX_PRIOR_EPISODES));

        if (reviews.isEmpty()) {
            return List.of();
        }

        List<PriorAcceptedDiagnosisDTO> out = new ArrayList<>(reviews.size());
        for (DoctorRecommendationReview review : reviews) {
            try {
                out.add(buildDto(review));
            } catch (Exception e) {
                log.warn("Skipping prior review id={} for episode={} — {}",
                        review.getId(), currentEpisodeId, e.getMessage());
            }
        }
        return out;
    }

    /**
     * MapStruct handles field-by-field mapping; this method orchestrates the
     * side queries (items for the run) and the doctor-override business rule.
     */
    private PriorAcceptedDiagnosisDTO buildDto(DoctorRecommendationReview review) {
        PriorAcceptedDiagnosisDTO dto = mapper.toDto(review);

        AiRecommendationRun run = review.getRun();
        Map<String, Object> modifications = review.getModificationJson();
        List<AiRecommendationItem> items = run != null
                ? itemRepository.findByRunIdOrderByPriorityOrderAsc(run.getId())
                : List.of();

        List<PriorAcceptedDiagnosisDTO.AcceptedItem> acceptedItems = new ArrayList<>(items.size());
        for (AiRecommendationItem item : items) {
            PriorAcceptedDiagnosisDTO.AcceptedItem mapped = mapper.toAcceptedItem(item);
            applyDoctorOverride(mapped, item, modifications);
            acceptedItems.add(mapped);
        }
        dto.setAcceptedItems(acceptedItems);
        return dto;
    }

    private void applyDoctorOverride(
            PriorAcceptedDiagnosisDTO.AcceptedItem target,
            AiRecommendationItem item,
            Map<String, Object> modificationJson) {

        Object override = DoctorModificationUtils.extractOverride(
                modificationJson, target.getCategory(), item.getId());
        if (override != null) {
            target.setFinalItemJson(override);
            target.setSource("DOCTOR_MODIFIED");
        } else {
            target.setFinalItemJson(JsonUtils.safeReadMap(objectMapper, item.getItemJson()));
            target.setSource("AI_ORIGINAL");
        }
    }
}
