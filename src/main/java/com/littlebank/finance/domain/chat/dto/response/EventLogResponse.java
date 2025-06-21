package com.littlebank.finance.domain.chat.dto.response;

import com.littlebank.finance.domain.chat.domain.ChatRoomEventLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class EventLogResponse {
    private String message;
    private LocalDateTime timestamp;

    public static EventLogResponse of(String message, ChatRoomEventLog eventLog) {
        return EventLogResponse.builder()
                .message(message)
                .timestamp(eventLog.getCreatedDate())
                .build();
    }

}
