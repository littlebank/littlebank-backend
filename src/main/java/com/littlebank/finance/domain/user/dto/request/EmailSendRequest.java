package com.littlebank.finance.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EmailSendRequest {
    @Email
    @NotBlank
    @Schema(description = "확인용 메일을 보낼 이메일", example = "example@gmail.com")
    private String toEmail;
}
