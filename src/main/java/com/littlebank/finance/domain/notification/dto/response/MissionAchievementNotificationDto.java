package com.littlebank.finance.domain.notification.dto.response;

import lombok.Getter;

@Getter
public class MissionAchievementNotificationDto {
    private Long parentId;
    private String childNickname;
    private String missionTitle;
    private Long missionId;
}
