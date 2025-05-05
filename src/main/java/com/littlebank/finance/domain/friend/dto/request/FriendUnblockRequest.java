package com.littlebank.finance.domain.friend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FriendUnblockRequest {
    @NotNull
    @Schema(description = "차단 해제할 friend id", example = "1")
    private Long friendId;
}
