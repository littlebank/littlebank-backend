package com.littlebank.finance.domain.ranking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GoalRankingResponseDto {
    private Long userId;
    private String name;
    private int missionTotal;
    private int missionCompleted;
    private int missionReward;
    private int challengeTotal;
    private int challengeCompleted;
    private int challengeReward;
    private int goalTotal;
    private int goalCompleted;
    private int goalReward;


    public int getTotalReward() {
        return missionReward + challengeReward + goalReward;
    }
}
