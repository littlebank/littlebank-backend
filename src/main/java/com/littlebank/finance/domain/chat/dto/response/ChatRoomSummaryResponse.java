package com.littlebank.finance.domain.chat.dto.response;

import com.littlebank.finance.domain.chat.domain.RoomRange;
import com.littlebank.finance.domain.chat.domain.RoomType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ChatRoomSummaryResponse {
    private Long userChatRoomId;
    private Long roomId;
    private String roomName;
    private RoomType roomType;
    private RoomRange roomRange;
    private List<String> participantNameList;
    private LocalDateTime displayIdx;
}
