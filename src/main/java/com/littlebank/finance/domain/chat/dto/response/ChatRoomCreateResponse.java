package com.littlebank.finance.domain.chat.dto.response;

import com.littlebank.finance.domain.chat.domain.ChatRoom;
import com.littlebank.finance.domain.chat.domain.RoomRange;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatRoomCreateResponse {
    private Long roomId;
    private String roomName;
    private RoomRange roomRange;
    private Long createdById;

    public static ChatRoomCreateResponse of(ChatRoom chatRoom) {
        return ChatRoomCreateResponse.builder()
                .roomId(chatRoom.getId())
                .roomName(chatRoom.getName())
                .roomRange(chatRoom.getRange())
                .createdById(chatRoom.getCreatedBy().getId())
                .build();
    }
}
