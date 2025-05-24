package com.littlebank.finance.domain.goal.dto.response;

import com.littlebank.finance.domain.goal.domain.Goal;
import com.littlebank.finance.domain.goal.domain.GoalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GoalApplyResponse {
    private Long goalId;
    private GoalStatus status;

    public static GoalApplyResponse of(Goal goal) {
        return GoalApplyResponse.builder()
                .goalId(goal.getId())
                .status(goal.getStatus())
                .build();
    }
}
