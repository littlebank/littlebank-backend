package com.littlebank.finance.domain.point.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ChildPointRefundRequest {
    @NotNull
    @Schema(description = "핸드폰 번호에 대응되는 유저 식별 id")
    private Long depositTargetUserId;
    @NotNull
    @Schema(description = "환전 신청 금액")
    private Integer exchangeAmount;
}
