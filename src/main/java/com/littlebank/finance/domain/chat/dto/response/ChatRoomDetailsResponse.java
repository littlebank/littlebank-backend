package com.littlebank.finance.domain.chat.dto.response;

import com.littlebank.finance.domain.chat.domain.RoomRange;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ChatRoomDetailsResponse {
    private Long roomId;
    private String roomName;
    private RoomRange roomRange;
    private List<ParticipantInfo> participants;

    @Getter
    @AllArgsConstructor
    public static class ParticipantInfo {
        private Long userId;
        private String name;
        private String profileImageUrl;
        private Boolean isFriend;
        private Long friendId;
        private String customName;
        private Boolean isBestFriend;
        private Boolean isBlocked;
    }
}
