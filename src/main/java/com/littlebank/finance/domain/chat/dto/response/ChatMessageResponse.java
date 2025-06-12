package com.littlebank.finance.domain.chat.dto.response;

import com.littlebank.finance.domain.chat.domain.ChatMessage;
import com.littlebank.finance.domain.chat.domain.MessageType;
import com.littlebank.finance.domain.friend.domain.Friend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ChatMessageResponse {
    private Long messageId;
    private Long roomId;
    private String content;
    private MessageType messageType;
    private Long senderUserId;
    private String senderName;
    private String senderProfileImageUrl;
    private LocalDateTime timestamp;
    private Boolean isFriend;
    private String customName;
    private Boolean isBestFriend;
    private Boolean isBlocked;

    public static ChatMessageResponse of(ChatMessage message, Friend friend) {
        return ChatMessageResponse.builder()
                .messageId(message.getId())
                .roomId(message.getRoom().getId())
                .content(message.getContent())
                .messageType(message.getMessageType())
                .senderUserId(message.getSender().getId())
                .senderName(message.getSender().getName())
                .senderProfileImageUrl(message.getSender().getProfileImagePath())
                .timestamp(message.getTimestamp())
                .isFriend(friend != null)
                .customName(friend != null ? friend.getCustomName() : null)
                .isBestFriend(friend != null && friend.getIsBestFriend())
                .isBlocked(friend != null && friend.getIsBlocked())
                .build();
    }
}
