package com.littlebank.finance.domain.chat.dto.response;

import com.littlebank.finance.domain.chat.domain.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class APIMessageResponse {
    private Long messageId;
    private String content;
    private MessageType messageType;
    private LocalDateTime timestamp;
    private Integer readCount;
    private Long senderUserId;
    private String senderName;
    private String senderProfileImageUrl;
    private Boolean isFriend;
    private String customName;
    private Boolean isBestFriend;
    private Boolean isBlocked;
}
