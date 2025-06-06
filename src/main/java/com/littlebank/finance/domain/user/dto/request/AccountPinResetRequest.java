package com.littlebank.finance.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AccountPinResetRequest {
    @NotBlank
    @Schema(description = "검증할 pin번호", example = "123456")
    private String pin;
}
