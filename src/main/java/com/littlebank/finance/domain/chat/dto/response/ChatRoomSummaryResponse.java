package com.littlebank.finance.domain.chat.dto.response;

import com.littlebank.finance.domain.chat.domain.RoomRange;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ChatRoomSummaryResponse {
    private Long roomId;
    private String roomName;
    private RoomRange roomRange;
    private List<String> participantNameList;
    private LocalDateTime displayIdx;
    private Integer unreadMessageCount;
}
