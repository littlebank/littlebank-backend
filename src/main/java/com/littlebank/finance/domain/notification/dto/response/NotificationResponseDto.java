package com.littlebank.finance.domain.notification.dto.response;

import com.littlebank.finance.domain.notification.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class NotificationResponseDto {
    private Long id;
    private String message;
    private NotificationType type;
    private LocalDateTime createdDate;
    private boolean isRead;
    public static NotificationResponseDto of (Long id, String message, NotificationType type, LocalDateTime createdDate, boolean isRead) {
        return NotificationResponseDto.builder()
                .id(id)
                .message(message)
                .type(type)
                .createdDate(createdDate)
                .isRead(isRead)
                .build();
    }
}

