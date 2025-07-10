package com.littlebank.finance.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DuplicatedEmailCheckRequest {
    @Email
    @NotBlank
    @Schema(description = "중복 검증할 이메일", example = "example@gmail.com")
    private String email;
}
