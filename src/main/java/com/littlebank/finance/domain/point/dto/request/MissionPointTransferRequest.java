package com.littlebank.finance.domain.point.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MissionPointTransferRequest {
    @NotNull
    @Schema(description = "포인트를 보낼 유저")
    private Long receiverId;
    @NotNull
    @Schema(description = "이체할 포인트 양")
    private Integer pointAmount;
    @NotNull
    @Schema(description = "이체 메시지(메시지가 없다면 공백)")
    private String message;
    @NotNull
    @Schema(description = "포인트 지급 대상 미션 식별 id")
    private Long missionId;
    @NotNull
    @Schema(description = "보상 지급 여부")
    private Boolean isRefused;
}
