package com.littlebank.finance.domain.chat.dto.request;

import com.littlebank.finance.domain.chat.domain.MessageType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDto {
    @NotBlank
    @Schema(description = "채팅방 ID", example = "room-uuid-1234", required = true)
    private String roomId;

    @NotNull
    @Schema(description = "보내는 사용자 ID", example = "1", required = true)
    private Long senderId;

    @Schema(description = "받는 사용자 ID (1:1 채팅인 경우에 사용)", example = "2")
    private Long receiverId;

    @NotBlank
    @Schema(description = "채팅 메시지 내용", example = "안녕하세요!", required = true)
    private String message;

    @NotNull
    @Schema(description = "메시지 타입 (ENTER, TALK, LEAVE 중 하나)", example = "TALK", required = true)
    private MessageType type;
}