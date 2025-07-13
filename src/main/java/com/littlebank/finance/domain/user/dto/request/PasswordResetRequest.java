package com.littlebank.finance.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PasswordResetRequest {
    @NotBlank
    @Schema(description = "현재 비밀번호")
    private String currentPassword;
    @NotBlank
    @Schema(description = "변경할 비밀번호")
    private String newPassword;
}
