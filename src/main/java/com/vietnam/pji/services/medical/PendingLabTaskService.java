package com.vietnam.pji.services.medical;

import com.vietnam.pji.model.agentic.PendingLabTask;

import java.util.List;
import java.util.Map;

public interface PendingLabTaskService {

    List<PendingLabTask> getMyPendingTasks(Long userId);

    long getMyPendingCount(Long userId);

    void dismiss(Long taskId);

    void fulfillByQuickEntry(Long taskId, Object value, String unit);

    void createFromCompleteness(Long episodeId, Long patientId, Long userId,
            Long runId, List<Map<String, Object>> missingItems);

    void autoFulfillForEpisode(Long episodeId, Long labResultId,
            List<Map<String, Object>> hematologyTests,
            List<Map<String, Object>> fluidAnalysis,
            Map<String, Object> biochemicalData);

    /**
     * Fulfil pending clinical/culture tasks (sinus tract, infection type,
     * implant stability, allergies, histology, culture count) once the episode
     * aggregate save has persisted the relevant records. Lab tasks are handled
     * separately by {@link #autoFulfillForEpisode}.
     */
    void autoFulfillClinicalForEpisode(Long episodeId);
}
