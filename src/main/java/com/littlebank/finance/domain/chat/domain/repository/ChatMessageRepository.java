package com.littlebank.finance.domain.chat.domain.repository;

import com.littlebank.finance.domain.chat.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>, CustomChatMessageRepository {
    void updateDisplayIdxByRoomId(Long roomId);
}
