package com.littlebank.finance.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Schema(description = "채팅방 요약 정보 DTO")
public class ChatRoomSummary {

    @Schema(description = "채팅방 ID", example = "room-uuid-1234")
    private String roomId;

    @Schema(description = "채팅방 이름", example = "ltbk 채팅방")
    private String roomName;

    @Schema(description = "최근 메시지 내용", example = "안녕~")
    private String lastMessage;

    @Schema(description = "최근 메시지 전송 시간", example = "2024-04-21T13:45:00")
    private LocalDateTime lastMessageTime;

    @Schema(description = "읽지 않은 메시지 수", example = "2")
    private Long unreadCount;
}