package com.littlebank.finance.domain.chat.dto.request;

import com.littlebank.finance.domain.chat.domain.MessageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDto {
    private String roomId;
    private Long senderId;
    private Long receiverId;
    private String message;
    private MessageType type;
}