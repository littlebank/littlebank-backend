package com.littlebank.finance.domain.chat.domain.repository;

import com.littlebank.finance.domain.chat.domain.UserChatRoom;
import com.littlebank.finance.domain.chat.dto.response.ChatRoomSummaryResponse;

import java.util.List;

public interface CustomUserChatRoomRepository {
    void updateDisplayIdxByRoomId(Long roomId);
    int countParticipantsExcludingUser(Long roomId, Long excludedUserId);

    List<UserChatRoom> findAllWithFetchByRoomId(Long roomId);
    List<ChatRoomSummaryResponse> findChatRoomSummaryList(Long userId);
}
