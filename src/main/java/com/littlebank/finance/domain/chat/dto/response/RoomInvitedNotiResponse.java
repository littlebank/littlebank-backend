package com.littlebank.finance.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RoomInvitedNotiResponse {
    private Long roomId;

    public static RoomInvitedNotiResponse of(Long roomId) {
        return RoomInvitedNotiResponse.builder()
                .roomId(roomId)
                .build();
    }
}
