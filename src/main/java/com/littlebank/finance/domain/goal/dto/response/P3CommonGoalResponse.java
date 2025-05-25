package com.littlebank.finance.domain.goal.dto.response;

import com.littlebank.finance.domain.goal.domain.Goal;
import com.littlebank.finance.domain.goal.domain.GoalCategory;
import com.littlebank.finance.domain.goal.domain.GoalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class P3CommonGoalResponse {
    private Long goalId;
    private String title;
    private GoalCategory category;
    private GoalStatus status;

    public static P3CommonGoalResponse of(Goal goal) {
        return P3CommonGoalResponse.builder()
                .goalId(goal.getId())
                .title(goal.getTitle())
                .category(goal.getCategory())
                .status(goal.getStatus())
                .build();
    }
}
