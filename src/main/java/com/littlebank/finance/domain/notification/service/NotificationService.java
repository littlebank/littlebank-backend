package com.littlebank.finance.domain.notification.service;

import com.littlebank.finance.domain.feed.domain.Feed;
import com.littlebank.finance.domain.notification.domain.Notification;
import com.littlebank.finance.domain.notification.domain.NotificationType;
import com.littlebank.finance.domain.notification.domain.repository.NotificationRepository;
import com.littlebank.finance.domain.notification.dto.response.NotificationResponseDto;
import com.littlebank.finance.domain.notification.exception.NotificationException;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public Page<NotificationResponseDto> getUserNotifications(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        return notificationRepository.findByReceiverOrderByCreatedDateDesc(user, pageable)
                .map(n -> NotificationResponseDto.builder()
                        .id(n.getId())
                        .message(n.getMessage())
                        .type(n.getType())
                        .createdDate(n.getCreatedDate())
                        .isRead(n.isRead())
                        .build());
    }
}
