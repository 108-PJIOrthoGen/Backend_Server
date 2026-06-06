package com.vietnam.pji.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Aggregate AI-vs-doctor consensus statistics across all doctor reviews.
 *
 * <p>Answers the two product questions behind the compare-result feature:
 * "Tỷ lệ bác sĩ đồng thuận với AI là bao nhiêu %?" and "Những ca nào bác sĩ
 * ghi đè kết quả của AI?". Also the analytical substrate for letting the AI
 * learn from the doctor's final word later on.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorReviewStatsDTO implements Serializable {

    private long totalReviews;
    private long accepted;
    private long modified;
    private long rejected;
    private long savedDraft;

    /**
     * Percentage of decided reviews (ACCEPTED + MODIFIED + REJECTED) where the
     * doctor fully accepted the AI recommendation. Null when nothing decided.
     */
    private Double consensusRate;

    /**
     * Average per-criterion agreement_rate (0-100) across reviews carrying an
     * agreement_json payload. Null when no review has one.
     */
    private Double avgAgreementRate;

    /** Cases where the doctor overrode the AI (MODIFIED or REJECTED). */
    private List<OverriddenCaseDTO> overriddenCases;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OverriddenCaseDTO implements Serializable {
        private Long reviewId;
        private Long episodeId;
        private Long runId;
        private String patientName;
        private String reviewStatus;
        private Double agreementRate;
        private String reviewNote;
        private Date updatedAt;
    }
}
