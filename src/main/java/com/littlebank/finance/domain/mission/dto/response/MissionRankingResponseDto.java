package com.littlebank.finance.domain.mission.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MissionRankingResponseDto {
    private Long friendUserId;
    private Long friendId;
    private String friendName;
    private boolean isBestFriend;

    private int totalMissionCount;
    private int completedMissionCount;
    private double completionRate;

    private List<LearningMissionStats> learningStats;
    private int habitMissionCount;
    private int habitCompletedCount;
    private double habitCompletionRate;

    public static MissionRankingResponseDto of (
            Long friendUserId,
            Long friendId,
            String friendName,
            boolean isBestFriend,
            int total,
            int completed,
            double rate,
            List<LearningMissionStats> learningStats,
            int habitTotal,
            int habitCompleted,
            double habitCompletionRate
    ) {
        return MissionRankingResponseDto.builder()
                .friendUserId(friendUserId)
                .friendId(friendId)
                .friendName(friendName)
                .isBestFriend(isBestFriend)
                .totalMissionCount(total)
                .completedMissionCount(completed)
                .completionRate(rate)
                .learningStats(learningStats)
                .habitMissionCount(habitTotal)
                .habitCompletedCount(habitCompleted)
                .habitCompletionRate(habitCompletionRate)
                .build();
    }
}
