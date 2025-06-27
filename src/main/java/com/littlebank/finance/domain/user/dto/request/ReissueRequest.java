package com.littlebank.finance.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReissueRequest {

    @NotBlank
    @Schema(description = "리프레시 토큰")
    private String refreshToken;
}
