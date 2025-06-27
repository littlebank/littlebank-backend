package com.littlebank.finance.domain.point.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PaymentInfoSaveRequest {
    @NotBlank
    @Schema(description = "결제 성공 후 받는 값")
    private String impUid;
}
