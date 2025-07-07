package com.littlebank.finance.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserDeleteRequest {
    @NotBlank
    @Schema(description = "탈퇴사유", example = "그냥요")
    private String reason;
}
