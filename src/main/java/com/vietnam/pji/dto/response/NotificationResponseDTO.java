package com.vietnam.pji.dto.response;

import java.io.Serializable;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vietnam.pji.constant.NotificationSeverity;
import com.vietnam.pji.constant.NotificationType;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationResponseDTO implements Serializable {
    private Long id;
    private NotificationType type;
    private String title;
    private String message;
    private String referenceId;
    private String linkUrl;
    private NotificationSeverity severity;
    private Boolean isRead;
    private Instant createdAt;
    private Instant readAt;
}
