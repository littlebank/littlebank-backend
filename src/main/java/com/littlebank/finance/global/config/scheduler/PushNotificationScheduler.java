package com.littlebank.finance.global.config.scheduler;

import com.littlebank.finance.domain.goal.service.GoalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushNotificationScheduler {
    private final FixPushNotificationService fixPushNotificationService;

    /**
     * [목표 달성 알림]
     * 매주 월요일 오전 8시
     * 부모에게 아이들의 달성한 목표에 대해 포인트 지급에 대한 알림
     */
    @Scheduled(cron = "0 0 8 * * 1")
    public void notifyParentOfChildGoalAchievements() {
        fixPushNotificationService.sendWeeklyGoalAchievementAlertToParents();
    }
    // 미션 - 9시, 챌린지 신청 - 20시, 챌린지 완료 - 9시
    @Scheduled(cron = "0 0 0 * * *")
    public void updateStatusAtMidnight() {
        fixPushNotificationService.updateExpiredMissionsAndChallengesStatus();
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void notifyParentsOfExpiredTasks() {
        fixPushNotificationService.notifyParentsOfCompletedMissionsAndChallenges();
    }
}
