package com.littlebank.finance.domain.chat.dto.response;

import com.littlebank.finance.domain.chat.domain.ChatMessage;
import com.littlebank.finance.domain.chat.domain.MessageType;
import com.littlebank.finance.domain.chat.domain.UserChatRoom;
import com.littlebank.finance.domain.friend.domain.Friend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ChatMessageResponse {
    private Long roomId;
    private LocalDateTime displayIdx;
    private Long messageId;
    private String content;
    private MessageType messageType;
    private LocalDateTime timestamp;
    private Long senderUserId;
    private String senderName;
    private String senderProfileImageUrl;
    private Boolean isFriend;
    private String customName;
    private Boolean isBestFriend;
    private Boolean isBlocked;

    public static ChatMessageResponse of(UserChatRoom participant, ChatMessage message, Friend friend) {
        return ChatMessageResponse.builder()
                .roomId(participant.getRoom().getId())
                .displayIdx(participant.getDisplayIdx())
                .messageId(message.getId())
                .content(message.getContent())
                .messageType(message.getMessageType())
                .timestamp(message.getTimestamp())
                .senderUserId(message.getSender().getId())
                .senderName(message.getSender().getName())
                .senderProfileImageUrl(message.getSender().getProfileImagePath())
                .isFriend(friend != null)
                .customName(friend != null ? friend.getCustomName() : null)
                .isBestFriend(friend != null && friend.getIsBestFriend())
                .isBlocked(friend != null && friend.getIsBlocked())
                .build();
    }
}
