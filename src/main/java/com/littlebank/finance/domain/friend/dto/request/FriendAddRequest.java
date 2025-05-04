package com.littlebank.finance.domain.friend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FriendAddRequest {
    @NotNull
    @Schema(description = "친구 추가할 유저 id", example = "1")
    private Long targetUserId;
}
