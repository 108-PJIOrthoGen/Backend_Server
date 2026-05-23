package com.vietnam.pji.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Compact, prompt-friendly snapshot of a prior AI recommendation run that the
 * doctor has reviewed (ACCEPTED or MODIFIED). Carried alongside the clinical
 * snapshot so the RAG service can keep subsequent diagnoses consistent with
 * what the physician has already validated for the same patient on previous
 * episodes.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriorAcceptedDiagnosisDTO implements Serializable {

    @JsonProperty("episode_id")
    private Long episodeId;

    @JsonProperty("run_id")
    private Long runId;

    @JsonProperty("admission_date")
    private String admissionDate;

    @JsonProperty("discharge_date")
    private String dischargeDate;

    @JsonProperty("review_status")
    private String reviewStatus;

    @JsonProperty("reviewed_at")
    private String reviewedAt;

    @JsonProperty("review_note")
    private String reviewNote;

    @JsonProperty("accepted_items")
    private List<AcceptedItem> acceptedItems;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AcceptedItem implements Serializable {

        private String category;
        private String title;

        @JsonProperty("is_primary")
        private Boolean isPrimary;

        @JsonProperty("priority_order")
        private Integer priorityOrder;

        /**
         * The final content the doctor accepted: doctor's modification when the
         * review was MODIFIED, otherwise the original AI item_json.
         */
        @JsonProperty("final_item_json")
        private Object finalItemJson;

        /** {@code AI_ORIGINAL} when the doctor accepted as-is, {@code DOCTOR_MODIFIED} when edited. */
        private String source;
    }
}
