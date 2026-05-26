package com.vietnam.pji.model.notification;

import java.time.Instant;

import com.vietnam.pji.constant.NotificationSeverity;
import com.vietnam.pji.constant.NotificationType;
import com.vietnam.pji.model.AbstractEntity;
import com.vietnam.pji.model.auth.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notification")
public class Notification extends AbstractEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 64, nullable = false)
    private NotificationType type;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "reference_id", length = 255)
    private String referenceId;

    @Column(name = "link_url", length = 512)
    private String linkUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", length = 32, nullable = false)
    @Builder.Default
    private NotificationSeverity severity = NotificationSeverity.INFO;

    @Column(name = "is_read", nullable = false)
    @Builder.Default
    private Boolean isRead = false;

    @Column(name = "read_at")
    private Instant readAt;
}
