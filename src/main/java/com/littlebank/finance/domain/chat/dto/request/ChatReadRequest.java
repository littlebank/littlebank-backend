package com.littlebank.finance.domain.chat.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ChatReadRequest {
    private Long roomId;
    private List<Long> messageIds;
}

