package com.littlebank.finance.domain.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ChatRoomNameUpdateRequest {
    @NotNull
    @Schema(description = "채팅방 식별 id", example = "1")
    private Long roomId;
    @NotBlank
    @Schema(description = "수정할 채팅방 이름", example = "Zl존 방")
    private String name;
}
