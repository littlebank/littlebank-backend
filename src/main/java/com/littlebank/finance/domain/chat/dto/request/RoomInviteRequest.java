package com.littlebank.finance.domain.chat.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class RoomInviteRequest {
    private Long roomId;
    private List<Long> targetUserIds;
}
