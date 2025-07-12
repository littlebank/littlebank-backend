package com.littlebank.finance.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CertificationCodeSendRequest {
    @NotBlank
    @Schema(description = "인증번호를 받을 전화번호", example = "01012345678")
    private String toNumber;
}
