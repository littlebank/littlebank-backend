package com.littlebank.finance.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ChatReadResponse {
    private List<Long> messageIds;

    public static ChatReadResponse of (List<Long> messageIds) {
        return ChatReadResponse.builder()
                .messageIds(messageIds)
                .build();
    }
}
