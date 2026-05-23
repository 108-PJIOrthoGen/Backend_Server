package com.vietnam.pji.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Message published to RabbitMQ for async AI recommendation processing.
 * Consumed by the Python RAG worker.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RabbitMQRecommendationMessage implements Serializable {

    private String requestId;
    private Long runId;
    private Long episodeId;
    private Long snapshotId;
    private String triggerType;
    private Long requestedBy;

    /** Full clinical snapshot — required by the Python RAG worker to process. */
    private Map<String, Object> snapshotDataJson;

    /** Options for the AI service (language, includeCitations, topK). */
    private Map<String, Object> options;

    /**
     * Physician-validated diagnoses from prior episodes for this patient. Used by
     * the RAG worker as grounding context so subsequent runs stay consistent with
     * what the doctor has already accepted. May be empty when no prior reviews exist.
     */
    private List<PriorAcceptedDiagnosisDTO> priorAcceptedDiagnoses;

    /** Retry counter — incremented by the Python worker on transient failures. */
    @Builder.Default
    private int retryCount = 0;
}
