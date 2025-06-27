package com.littlebank.finance.domain.share.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateLinkRequestDto {
    @NotNull
    @Schema(description = "대표자의 구독권 id")
    private Long subscriptionId;
    @NotNull
    @Schema(description = "카톡 친구 목록에서 클릭하는 사람 수")
    private int count;
}
