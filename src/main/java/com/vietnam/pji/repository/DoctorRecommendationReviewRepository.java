package com.vietnam.pji.repository;

import com.vietnam.pji.constant.ReviewStatus;
import com.vietnam.pji.model.agentic.DoctorRecommendationReview;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DoctorRecommendationReviewRepository
        extends JpaRepository<DoctorRecommendationReview, Long>,
        JpaSpecificationExecutor<DoctorRecommendationReview> {

    Optional<DoctorRecommendationReview> findByRunId(Long runId);

    List<DoctorRecommendationReview> findByEpisodeIdOrderByCreatedAtDesc(Long episodeId);

    /**
     * Fetch reviews that a doctor has signed off on (ACCEPTED or MODIFIED) for a
     * patient, excluding the current episode. Used to feed prior physician-validated
     * diagnoses into a new AI recommendation run for cross-episode consistency.
     *
     * <p>{@code sinceCutoff} is required (non-null): inlining a nullable
     * {@code (:p IS NULL OR ...)} guard makes PostgreSQL fail to infer the
     * parameter type. Callers should pass {@code new Date(0)} for "no cutoff".
     */
    @Query("""
            SELECT r FROM DoctorRecommendationReview r
            WHERE r.episode.patient.id = :patientId
              AND r.episode.id <> :excludeEpisodeId
              AND r.reviewStatus IN :acceptedStatuses
              AND r.createdAt >= :sinceCutoff
            ORDER BY r.createdAt DESC
            """)
    List<DoctorRecommendationReview> findAcceptedReviewsForPatientExcludingEpisode(
            @Param("patientId") Long patientId,
            @Param("excludeEpisodeId") Long excludeEpisodeId,
            @Param("acceptedStatuses") List<ReviewStatus> acceptedStatuses,
            @Param("sinceCutoff") Date sinceCutoff,
            Pageable pageable);
}
