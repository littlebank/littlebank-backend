package com.littlebank.finance.domain.chat.domain.repository;

public interface CustomChatMessageRepository {
    void updateDisplayIdxByRoomId(Long roomId);
}
