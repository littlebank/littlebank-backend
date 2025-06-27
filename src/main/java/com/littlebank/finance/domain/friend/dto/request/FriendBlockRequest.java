package com.littlebank.finance.domain.friend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FriendBlockRequest {
    @NotNull
    @Schema(description = "차단할 user id", example = "1")
    private Long targetUserId;
}
