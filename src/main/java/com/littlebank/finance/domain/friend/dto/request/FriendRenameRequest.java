package com.littlebank.finance.domain.friend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class FriendRenameRequest {
    @NotNull
    @Schema(description = "friend id", example = "1")
    private Long targetFriendId;
    @NotBlank
    @Size(max = 20, message = "이름은 20자 이하여야 합니다.")
    @Schema(description = "변경할 친구 이름", example = "Zl존 김동규")
    private String changeName;
}
