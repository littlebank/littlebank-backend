package com.littlebank.finance.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PasswordReissueRequest {
    @Email
    @NotBlank
    @Schema(description = "임시 비밀번호를 발급할 계정의 이메일")
    private String email;
}
