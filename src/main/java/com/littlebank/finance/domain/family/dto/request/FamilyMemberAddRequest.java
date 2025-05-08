package com.littlebank.finance.domain.family.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FamilyMemberAddRequest {
    @NotNull
    @Schema(description = "가족 맴버로 추가할 user id", example = "1")
    private Long targetUserId;
}
