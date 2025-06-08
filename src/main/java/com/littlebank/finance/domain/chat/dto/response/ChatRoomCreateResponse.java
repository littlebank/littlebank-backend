package com.littlebank.finance.domain.chat.dto.response;

import com.littlebank.finance.domain.chat.domain.ChatRoom;
import com.littlebank.finance.domain.chat.domain.RoomRange;
import com.littlebank.finance.domain.chat.domain.RoomType;
import com.littlebank.finance.domain.chat.domain.UserChatRoom;
import com.littlebank.finance.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ChatRoomCreateResponse {
    private Long roomId;
    private String roomName;
    private RoomType roomType;
    private RoomRange roomRange;
    private Long createdById;
    public static ChatRoomCreateResponse of(ChatRoom chatRoom) {
        return ChatRoomCreateResponse.builder()
                .roomId(chatRoom.getId())
                .roomName(chatRoom.getName())
                .roomType(chatRoom.getType())
                .roomRange(chatRoom.getRange())
                .createdById(chatRoom.getCreatedBy().getId())
                .build();
    }
}
