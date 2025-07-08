package com.littlebank.finance.domain.user.dto.request;

import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.constant.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SocialLoginAdditionalInfoRequest {

    @NotBlank
    @Schema(description = "핸드폰 번호", example = "01012345678")
    private String phone;

    @NotBlank
    @Schema(description = "생년월일 6자리", example = "020625")
    private String rrn;

    @NotNull
    @Schema(description = "유저 역할", example = "CHILD")
    private UserRole role;

    public User toEntity() {
        return User.builder()
                .phone(phone)
                .rrn(rrn)
                .role(role)
                .build();
    }
}
