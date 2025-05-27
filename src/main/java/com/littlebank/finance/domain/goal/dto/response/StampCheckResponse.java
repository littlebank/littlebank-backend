package com.littlebank.finance.domain.goal.dto.response;

import com.littlebank.finance.domain.goal.domain.Goal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StampCheckResponse {
    private Long goalId;
    private Boolean mon;
    private Boolean tue;
    private Boolean wed;
    private Boolean thu;
    private Boolean fri;
    private Boolean sat;
    private Boolean sun;

    public static StampCheckResponse of(Goal goal) {
        return StampCheckResponse.builder()
                .goalId(goal.getId())
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
