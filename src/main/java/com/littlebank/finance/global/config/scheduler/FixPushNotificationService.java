package com.littlebank.finance.global.config.scheduler;

import com.littlebank.finance.domain.challenge.domain.ChallengeParticipation;
import com.littlebank.finance.domain.challenge.domain.repository.ChallengeParticipationRepository;
import com.littlebank.finance.domain.goal.domain.repository.GoalRepository;
import com.littlebank.finance.domain.mission.domain.Mission;
import com.littlebank.finance.domain.mission.domain.MissionStatus;
import com.littlebank.finance.domain.mission.domain.repository.MissionRepository;
import com.littlebank.finance.domain.notification.domain.Notification;
import com.littlebank.finance.domain.notification.domain.NotificationType;
import com.littlebank.finance.domain.notification.domain.repository.NotificationRepository;
import com.littlebank.finance.domain.notification.dto.GoalAchievementNotificationDto;
import com.littlebank.finance.domain.notification.dto.response.AchievementNotificationResultDto;
import com.littlebank.finance.domain.notification.dto.response.ChallengeAchievementNotificationDto;
import com.littlebank.finance.domain.notification.dto.response.MissionAchievementNotificationDto;
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
    private final MissionRepository missionRepository;
    private final ChallengeParticipationRepository challengeParticipationRepository;

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

    public void updateExpiredMissionsAndChallengesStatus() {
        List<Mission> expiredMissions = missionRepository.updateExpiredMissionsToAchievement();
        List<ChallengeParticipation> expiredChallenges = challengeParticipationRepository.updateExpiredChallengesToAchievement();
        log.info("상태 갱신 완 - 미션 {}개, 챌린지 {}개", expiredMissions.size(), expiredChallenges.size());
        missionRepository.saveAll(expiredMissions);
        challengeParticipationRepository.saveAll(expiredChallenges);
    }

    public AchievementNotificationResultDto notifyParentsOfCompletedMissionsAndChallenges() {
        // 완료된 미션 알림
        List<MissionAchievementNotificationDto> missionResults = missionRepository.findMissionAchievementNotificationDto();
        try {
            missionResults.stream().forEach(
                    r -> {
                        User parent = userRepository.findById(r.getParentId())
                                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
                        Notification notification = notificationRepository.save(Notification.builder()
                                .receiver(parent)
                                .message(r.getChildNickname() + "의 미션 '" + r.getMissionTitle() + "'이(가) 종료되었어요!")
                                .subMessage("앱에서 아이에게 약속한 보상을 주세요~!")
                                .type(NotificationType.MISSION_ACHIEVEMENT)
                                .targetId(r.getMissionId())
                                .isRead(false)
                                .build());
                        firebaseService.sendNotification(notification);
                    }
            );
        } catch (DataIntegrityViolationException e) {
            log.warn("이미 동일한 알림이 존재합니다.");
        }

        // 완료된 챌린지 알림
        List<ChallengeAchievementNotificationDto> challengeResults = challengeParticipationRepository.findChallengeAchievementNotificationDto();
        try {
            challengeResults.stream().forEach(
                    r -> {
                        User parent = userRepository.findById(r.getParentId())
                                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
                        Notification notification = notificationRepository.save(Notification.builder()
                                .receiver(parent)
                                .message(r.getChildNickname() + "의 미션 '" + r.getChallengeTitle() + "'이(가) 종료되었어요!")
                                .subMessage("앱에서 아이에게 약속한 보상을 주세요~!")
                                .type(NotificationType.CHALLENGE_ACHIEVEMENT)
                                .targetId(r.getChallengeId())
                                .isRead(false)
                                .build());
                        firebaseService.sendNotification(notification);
                    }
            );
        } catch (DataIntegrityViolationException e) {
            log.warn("이미 동일한 알림이 존재합니다.");
        }
        return new AchievementNotificationResultDto(missionResults, challengeResults);
    }
}
