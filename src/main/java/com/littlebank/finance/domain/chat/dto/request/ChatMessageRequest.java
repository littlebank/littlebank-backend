package com.littlebank.finance.domain.chat.dto.request;

import com.littlebank.finance.domain.chat.domain.MessageType;
import lombok.Getter;

@Getter
public class ChatMessageRequest {
    private Long roomId;
    private String content;
    private MessageType messageType;
}
