package com.littlebank.finance.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoalAchievementNotificationDto {
    private Long parentId;
    private String nickname;
    private String title;
    private Long goalId;
    private Integer stampCount;
    private Long childId;
}
