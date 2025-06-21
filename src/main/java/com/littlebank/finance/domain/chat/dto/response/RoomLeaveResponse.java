package com.littlebank.finance.domain.chat.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RoomLeaveResponse {
    private Long roomId;
    private String message;
    private LocalDateTime timeStamp;
    private Long endOfDecreaseReadMarkMessageId;
}
