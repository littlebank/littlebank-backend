package com.littlebank.finance.domain.user.dto.request;

import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SocialLoginAdditionalInfoRequest {

    @NotBlank
    @Schema(description = "생년월일 6자리", example = "020625")
    private String rrn;

    @NotBlank
    @Schema(description = "유저 역할", example = "CHILD")
    private UserRole role;

    public User toEntity() {
        return User.builder()
                .rrn(rrn)
                .role(role)
                .build();
    }
}
