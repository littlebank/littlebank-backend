package com.littlebank.finance.domain.chat.dto.response;

import com.littlebank.finance.domain.chat.domain.ChatRoomEventLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class RoomInviteResponse {
    private Long roomId;
    private String message;
    private LocalDateTime timeStamp;

    public static RoomInviteResponse of(ChatRoomEventLog eventLog, String message) {
        return RoomInviteResponse.builder()
                .roomId(eventLog.getId())
                .message(message)
                .timeStamp(eventLog.getCreatedDate())
                .build();
    }

}
