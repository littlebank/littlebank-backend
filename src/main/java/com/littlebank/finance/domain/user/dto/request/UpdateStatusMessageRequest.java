package com.littlebank.finance.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateStatusMessageRequest {
    @NotNull
    @Size(max = 100, message = "상태 메시지는 100자 이하여야 합니다.")
    @Schema(description = "상태 메시지")
    private String statusMessage;
}
