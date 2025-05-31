package com.littlebank.finance.domain.mission.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MissionRecentRewardResponseDto {
    private Integer recentReward;
    public static MissionRecentRewardResponseDto of(Integer recentReward) {
        return MissionRecentRewardResponseDto.builder().recentReward(recentReward).build();
    }
}
