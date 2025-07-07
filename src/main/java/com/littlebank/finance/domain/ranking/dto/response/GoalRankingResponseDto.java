package com.littlebank.finance.domain.ranking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoalRankingResponseDto {
    private Long userId;
    private String name;

    private Long missionTotal;
    private Long missionCompleted;
    private Integer missionReward;

    private Long challengeTotal;
    private Long challengeCompleted;
    private Integer challengeReward;

    private Long goalTotal;
    private Long goalCompleted;
    private Integer goalReward;

    private Long entireTotal;
    private Long entireCompleted;
    private Integer entireReward;
    private Integer targetAmount;
    @Builder
    public GoalRankingResponseDto(Long userId, String name,
                                  Long missionTotal, Long missionCompleted, Integer missionReward,
                                  Long challengeTotal, Long challengeCompleted, Integer challengeReward,
                                  Long goalTotal, Long goalCompleted, Integer goalReward, Integer targetAmount) {
        this.userId = userId;
        this.name = name;
        this.missionTotal = missionTotal;
        this.missionCompleted = missionCompleted;
        this.missionReward = missionReward;
        this.challengeTotal = challengeTotal;
        this.challengeCompleted = challengeCompleted;
        this.challengeReward = challengeReward;
        this.goalTotal = goalTotal;
        this.goalCompleted = goalCompleted;
        this.goalReward = goalReward;
        this.entireTotal = sum(missionTotal, challengeTotal, goalTotal);
        this.entireCompleted = sum(missionCompleted, challengeCompleted, goalCompleted);
        this.entireReward = sum(missionReward, challengeReward, goalReward);
        this.targetAmount = targetAmount;
    }

    private Long sum(Long a, Long b, Long c) {
        return (a != null ? a : 0L) + (b != null ? b : 0L) + (c != null ? c : 0L);
    }

    private Integer sum(Integer a, Integer b, Integer c) {
        return (a != null ? a : 0) + (b != null ? b : 0) + (c != null ? c : 0);
    }
}
