package com.littlebank.finance.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CertificationCodeVerifyRequest {
    @NotBlank
    @Schema(description = "인증번호를 받을 전화번호", example = "01012345678")
    private String toNumber;
    @NotBlank
    @Schema(description = "검증할 인증코드", example = "123456")
    private String code;
}
