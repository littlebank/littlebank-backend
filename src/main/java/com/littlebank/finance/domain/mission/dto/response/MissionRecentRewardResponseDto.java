package com.littlebank.finance.domain.mission.dto.response;

import com.littlebank.finance.domain.mission.domain.MissionCategory;
import com.littlebank.finance.domain.mission.domain.MissionSubject;
import com.littlebank.finance.domain.mission.domain.MissionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MissionRecentRewardResponseDto {
    private MissionType type;
    private MissionCategory category;
    private MissionSubject subject;
    private Integer recentReward;
    public static MissionRecentRewardResponseDto of(MissionType type, MissionCategory category, MissionSubject subject, Integer recentReward) {
        return MissionRecentRewardResponseDto.builder()
                .type(type)
                .category(category)
                .subject(subject)
                .recentReward(recentReward)
                .build();
    }
}
