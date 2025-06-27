package com.littlebank.finance.domain.chat.domain.repository;

import com.littlebank.finance.domain.chat.domain.UserChatRoom;
import com.littlebank.finance.domain.chat.dto.response.ChatRoomDetailsResponse;
import com.littlebank.finance.domain.chat.dto.response.ChatRoomSummaryResponse;

import java.util.List;
import java.util.Optional;

public interface CustomUserChatRoomRepository {
    void updateDisplayIdxByRoomId(Long roomId);
    int countParticipantsExcludingUser(Long roomId, Long excludedUserId);

    List<UserChatRoom> findAllWithFetchByRoomId(Long roomId);
    List<UserChatRoom> findAllWithFetchByRoomIdNotInTargetUserIds(Long roomId, List<Long> targetUserIds);
    List<ChatRoomSummaryResponse> findChatRoomSummaryList(Long userId);
    Optional<ChatRoomDetailsResponse> findChatRoomDetails(Long userId, Long roomId);

}
