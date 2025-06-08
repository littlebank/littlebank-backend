package com.littlebank.finance.domain.notification.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChallengeAchievementNotificationDto {
    private Long parentId;
    private String childNickname;
    private String challengeTitle;
    private Long challengeId;
}
