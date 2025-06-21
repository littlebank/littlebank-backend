package com.littlebank.finance.domain.chat.domain.repository;

import com.littlebank.finance.domain.chat.domain.ChatRoomEventLog;

import java.util.List;

public interface CustomChatRoomEventLogRepository {

    List<ChatRoomEventLog> findAllByRoomId(Long userId, Long roomId);
    List<ChatRoomEventLog> findByRoomIdAndMessageIds(Long userId, Long roomId, Long startMessageId, Long endMessageId);
}
