package com.vietnam.pji.repository;

import com.vietnam.pji.constant.PendingLabTaskStatus;
import com.vietnam.pji.model.agentic.PendingLabTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PendingLabTaskRepository extends JpaRepository<PendingLabTask, Long>,
        JpaSpecificationExecutor<PendingLabTask> {

    List<PendingLabTask> findByAssignedToUserIdAndStatusOrderByCreatedAtDesc(
            Long userId, PendingLabTaskStatus status);

    // Tooltip + in-episode tab need fulfilled tasks alongside pending ones so
    // they can render per-episode progress (e.g. "2/5 done"). Dismissed tasks
    // are intentionally excluded.
    List<PendingLabTask> findByAssignedToUserIdAndStatusInOrderByCreatedAtDesc(
            Long userId, Collection<PendingLabTaskStatus> statuses);

    List<PendingLabTask> findByEpisodeIdAndStatus(Long episodeId, PendingLabTaskStatus status);

    Optional<PendingLabTask> findByEpisodeIdAndFieldAndStatus(
            Long episodeId, String field, PendingLabTaskStatus status);

    long countByAssignedToUserIdAndStatus(Long userId, PendingLabTaskStatus status);

    // Badge counts DISTINCT episodes that still have at least one pending task —
    // an episode drops off (badge -1) only once ALL its fields are fulfilled.
    @Query("SELECT COUNT(DISTINCT t.episode.id) FROM PendingLabTask t "
            + "WHERE t.assignedToUserId = :userId AND t.status = :status")
    long countDistinctEpisodesByAssignedToUserIdAndStatus(
            @Param("userId") Long userId,
            @Param("status") PendingLabTaskStatus status);
}
