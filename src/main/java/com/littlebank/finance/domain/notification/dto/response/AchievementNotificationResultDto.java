package com.littlebank.finance.domain.notification.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.checkerframework.checker.units.qual.A;

import java.util.List;

@Getter
@AllArgsConstructor
public class AchievementNotificationResultDto {
    private List<MissionAchievementNotificationDto> missionResults;
    private List<ChallengeAchievementNotificationDto> challengeResults;
}
