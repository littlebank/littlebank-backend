package com.littlebank.finance.domain.chat.domain.repository;

import com.littlebank.finance.domain.chat.dto.response.ChatRoomSummaryResponse;

import java.util.List;

public interface CustomUserChatRoomRepository {
    List<ChatRoomSummaryResponse> findChatRoomSummaryList(Long userId);
}
