package com.littlebank.finance.domain.chat.dto.response;

import com.littlebank.finance.domain.chat.domain.UserChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatRoomNameUpdateResponse {
    private Long roomId;
    private String name;

    public static ChatRoomNameUpdateResponse of(UserChatRoom userChatRoom) {
        return ChatRoomNameUpdateResponse.builder()
                .roomId(userChatRoom.getId())
                .name(userChatRoom.getCustomRoomName())
                .build();
    }
}
