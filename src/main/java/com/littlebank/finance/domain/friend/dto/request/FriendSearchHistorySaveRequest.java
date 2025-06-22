package com.littlebank.finance.domain.friend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FriendSearchHistorySaveRequest {
    @NotNull
    @Schema(description = "검색한 친구의 식별 id", example = "1")
    private Long friendId;
}
