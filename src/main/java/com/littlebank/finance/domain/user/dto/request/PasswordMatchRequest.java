package com.littlebank.finance.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PasswordMatchRequest {
    @NotBlank
    @Schema(description = "검증할 패스워드")
    private String password;
}
