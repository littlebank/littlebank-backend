package com.littlebank.finance.domain.mission.dto.request;

import com.littlebank.finance.domain.mission.domain.MissionCategory;
import com.littlebank.finance.domain.mission.domain.MissionSubject;
import com.littlebank.finance.domain.mission.domain.MissionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MissionRecentRewardRequestDto {
    @NotNull
    @Schema(description = "미션 유형", example = "학원미션/가족미션")
    private MissionType type;

    @NotNull
    @Schema(description = "미션 카테고리", example = "학습인증/습관형성")
    private MissionCategory category;

    @NotNull
    @Schema(description = "미션 수행 과목", example = "수학")
    private MissionSubject subject;

//    @NotNull
//    @Schema(description = "최근 보상 내역", example = "10000")
//    private Integer recentReward;


}
