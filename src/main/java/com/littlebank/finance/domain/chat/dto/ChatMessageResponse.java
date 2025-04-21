package com.littlebank.finance.domain.chat.dto;

import com.littlebank.finance.domain.chat.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponse  {
    private Long id;
    private String sender;
    private String message;
    private String type;
    private boolean isRead;
    private LocalDateTime createdAt;


    public static ChatMessageResponse from(ChatMessage message) {
        return ChatMessageResponse.builder()
                .id(message.getId())
                .sender(message.getSender().getName())
                .message(message.getMessage())
                .type(message.getType().name())
                .isRead(message.isRead())
                .createdAt(message.getCreatedAt())
                .build();
    }
}