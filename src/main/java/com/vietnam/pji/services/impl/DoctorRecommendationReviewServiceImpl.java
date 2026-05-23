package com.vietnam.pji.services.impl;

import com.vietnam.pji.constant.ReviewStatus;
import com.vietnam.pji.dto.request.DoctorRecommendationReviewRequestDTO;
import com.vietnam.pji.exception.ForbiddenException;
import com.vietnam.pji.exception.ResourceNotFoundException;
import com.vietnam.pji.model.agentic.AiRecommendationRun;
import com.vietnam.pji.model.agentic.DoctorRecommendationReview;
import com.vietnam.pji.model.auth.User;
import com.vietnam.pji.model.medical.PjiEpisode;
import com.vietnam.pji.repository.AiRecommendationRunRepository;
import com.vietnam.pji.repository.DoctorRecommendationReviewRepository;
import com.vietnam.pji.repository.EpisodeRepository;
import com.vietnam.pji.services.DoctorRecommendationReviewService;
import com.vietnam.pji.services.UserService;
import com.vietnam.pji.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorRecommendationReviewServiceImpl implements DoctorRecommendationReviewService {

    private final DoctorRecommendationReviewRepository reviewRepository;
    private final AiRecommendationRunRepository runRepository;
    private final EpisodeRepository episodeRepository;
    private final UserService userService;

    @Override
    @Transactional
    public DoctorRecommendationReview createOrUpdateReview(Long episodeId,
            DoctorRecommendationReviewRequestDTO request) {
        PjiEpisode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new ResourceNotFoundException("Episode not found with id: " + episodeId));

        AiRecommendationRun run = runRepository.findById(request.getRunId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "AI Recommendation Run not found with id: " + request.getRunId()));

        validateReviewAccess(episode, run);

        ReviewStatus status = ReviewStatus.valueOf(request.getReviewStatus());

        // Upsert: update existing review for this run, or create new
        DoctorRecommendationReview review = reviewRepository.findByRunId(request.getRunId())
                .orElse(DoctorRecommendationReview.builder()
                        .episode(episode)
                        .run(run)
                        .build());

        review.setReviewStatus(status);
        review.setReviewNote(request.getReviewNote());
        review.setRejectionReason(request.getRejectionReason());

        review.setModificationJson(request.getModificationJson());

        DoctorRecommendationReview saved = reviewRepository.save(review);
        eagerInit(saved);
        return saved;
    }

    private void validateReviewAccess(PjiEpisode episode, AiRecommendationRun run) {
        if (run.getEpisode() == null || !episode.getId().equals(run.getEpisode().getId())) {
            throw new ForbiddenException("AI recommendation run does not belong to this episode");
        }

        String currentEmail = SecurityUtils.getCurrentUserLogin().orElse("");
        if (isBlank(currentEmail)) {
            throw new ForbiddenException("You don't have permission to review this treatment plan");
        }

        if (isAdmin(currentEmail)) {
            return;
        }

        String patientCreatedBy = episode.getPatient() != null ? episode.getPatient().getCreatedBy() : null;
        String episodeCreatedBy = episode.getCreatedBy();
        String runCreatedBy = run.getCreatedBy();
        boolean hasOwnerMetadata = !isBlank(patientCreatedBy) || !isBlank(episodeCreatedBy) || !isBlank(runCreatedBy);

        if (hasOwnerMetadata
                && !sameUser(currentEmail, patientCreatedBy)
                && !sameUser(currentEmail, episodeCreatedBy)
                && !sameUser(currentEmail, runCreatedBy)) {
            throw new ForbiddenException("Only the owner of this medical record can review this treatment plan");
        }
    }

    private boolean isAdmin(String email) {
        User user = userService.handleGetUserByUsername(email);
        String roleName = user != null && user.getRole() != null ? user.getRole().getName() : "";
        return "ADMIN".equalsIgnoreCase(roleName) || "SUPER_ADMIN".equalsIgnoreCase(roleName);
    }

    private boolean sameUser(String currentEmail, String ownerEmail) {
        return !isBlank(ownerEmail) && currentEmail.trim().equalsIgnoreCase(ownerEmail.trim());
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorRecommendationReview getReviewByRunId(Long runId) {
        DoctorRecommendationReview review = reviewRepository.findByRunId(runId).orElse(null);
        if (review != null) {
            eagerInit(review);
        }
        return review;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorRecommendationReview> getReviewsByEpisodeId(Long episodeId) {
        List<DoctorRecommendationReview> reviews = reviewRepository.findByEpisodeIdOrderByCreatedAtDesc(episodeId);
        reviews.forEach(this::eagerInit);
        return reviews;
    }

    private void eagerInit(DoctorRecommendationReview review) {
        Hibernate.initialize(review.getEpisode());
        if (review.getEpisode() != null) {
            Hibernate.initialize(review.getEpisode().getPatient());
        }
        Hibernate.initialize(review.getRun());
        if (review.getRun() != null) {
            Hibernate.initialize(review.getRun().getEpisode());
            if (review.getRun().getEpisode() != null) {
                Hibernate.initialize(review.getRun().getEpisode().getPatient());
            }
            Hibernate.initialize(review.getRun().getSnapshot());
            if (review.getRun().getSnapshot() != null) {
                Hibernate.initialize(review.getRun().getSnapshot().getEpisode());
            }
        }
    }
}
