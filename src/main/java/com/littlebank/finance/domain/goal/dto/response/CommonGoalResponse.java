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
public class CommonGoalResponse {
    private Long goalId;
    private String title;
    private GoalCategory category;
    private Integer reward;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private GoalStatus status;
    private Long createdById;
    private Long familyId;
    private Boolean mon;
    private Boolean tue;
    private Boolean wed;
    private Boolean thu;
    private Boolean fri;
    private Boolean sat;
    private Boolean sun;

    public static CommonGoalResponse of(Goal goal) {
        return CommonGoalResponse.builder()
                .goalId(goal.getId())
                .title(goal.getTitle())
                .category(goal.getCategory())
                .reward(goal.getReward())
                .startDate(goal.getStartDate())
                .endDate(goal.getEndDate())
                .status(goal.getStatus())
                .createdById(goal.getCreatedBy().getId())
                .familyId(goal.getFamily().getId())
                .mon(goal.getMon())
                .tue(goal.getTue())
                .wed(goal.getWed())
                .thu(goal.getThu())
                .fri(goal.getFri())
                .sat(goal.getSat())
                .sun(goal.getSun())
                .build();
    }
}
