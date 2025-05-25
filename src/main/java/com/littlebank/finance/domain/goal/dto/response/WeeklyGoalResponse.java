package com.littlebank.finance.domain.goal.dto.response;

import com.littlebank.finance.domain.goal.domain.Goal;
import com.littlebank.finance.domain.goal.domain.GoalCategory;
import com.littlebank.finance.domain.goal.domain.GoalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class WeeklyGoalResponse {
    private Long goal_id;
    private String title;
    private GoalCategory category;
    private Integer reward;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private GoalStatus status;

    public static WeeklyGoalResponse of(Goal goal) {
        return WeeklyGoalResponse.builder()
                .goal_id(goal.getId())
                .title(goal.getTitle())
                .category(goal.getCategory())
                .reward(goal.getReward())
                .startDate(goal.getStartDate())
                .endDate(goal.getEndDate())
                .status(goal.getStatus())
                .build();
    }
}
