package com.littlebank.finance.domain.point.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ConfirmPaymentRequest {
    @NotBlank
    @Schema(description = "결제 요청 성공 후 받은 paymentKey 값")
    private String paymentKey;
    @NotBlank
    @Schema(description = "결제 요청 성공 후 받은 orderId 값", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    private String orderId;
    @NotNull
    @Schema(description = "결제 금액", example = "3000")
    private Integer amount;
}
