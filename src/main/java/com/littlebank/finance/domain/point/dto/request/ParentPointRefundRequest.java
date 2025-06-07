package com.littlebank.finance.domain.point.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ParentPointRefundRequest {
    @NotNull
    @Schema(description = "환전 신청 금액")
    private Integer exchangeAmount;
}
