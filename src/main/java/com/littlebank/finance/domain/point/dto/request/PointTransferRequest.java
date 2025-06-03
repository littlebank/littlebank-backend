package com.littlebank.finance.domain.point.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PointTransferRequest {
    @NotNull
    @Schema(description = "포인트를 보낼 유저")
    private Long receiverId;
    @NotNull
    @Schema(description = "이체할 포인트 양")
    private Integer pointAmount;
    @NotNull
    @Schema(description = "이체 메시지(메시지가 없다면 공백)")
    private String message;
}
