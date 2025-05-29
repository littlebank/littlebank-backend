package com.littlebank.finance.global.config.scheduler;

import com.littlebank.finance.domain.goal.domain.repository.GoalRepository;
import com.littlebank.finance.domain.notification.domain.Notification;
import com.littlebank.finance.domain.notification.domain.NotificationType;
import com.littlebank.finance.domain.notification.domain.repository.NotificationRepository;
import com.littlebank.finance.domain.notification.dto.GoalAchievementNotificationDto;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.error.exception.ErrorCode;
import com.littlebank.finance.global.firebase.FirebaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FixPushNotificationService {
    private final GoalRepository goalRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final FirebaseService firebaseService;

    public List<GoalAchievementNotificationDto> sendWeeklyGoalAchievementAlertToParents() {
        List<GoalAchievementNotificationDto> results = goalRepository.findGoalAchievementNotificationDto();
        try {
            results.stream()
                    .forEach(r -> {
                        User parent = userRepository.findById(r.getParentId())
                                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

                        Notification notification = notificationRepository.save(Notification.builder()
                                .receiver(parent)
                                .message("지난 주, " + r.getNickname() + "(이)가 \"" + r.getTitle() + "\" 목표를 " + (r.getStampCount() * 100 / 7) + "% 달성했어요!")
                                .subMessage("앱에서 아이에게 약속한 보상을 주세요~!")
                                .type(NotificationType.GOAL_ACHIEVEMENT)
                                .targetId(r.getGoalId())
                                .isRead(false)
                                .build());

                        firebaseService.sendNotification(notification);
                    });
        } catch (DataIntegrityViolationException e) {
            log.warn("이미 동일한 알림이 존재합니다.");
        }
        return results;
    }
}
