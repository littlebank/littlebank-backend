package com.littlebank.finance.domain.mission.dto.response;

import com.littlebank.finance.domain.mission.domain.Mission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MissionFinishScoreResponseDto {
    private Long missionId;
    private Integer score;

    public static MissionFinishScoreResponseDto of(Mission mission, Integer score) {
        return MissionFinishScoreResponseDto.builder()
                .missionId(mission.getId())
                .score(score)
                .build();
    }
}
