package com.littlebank.finance.domain.chat.domain.repository;

import com.littlebank.finance.domain.chat.domain.ChatRoomEventLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomEventLogRepository extends JpaRepository<ChatRoomEventLog, Long>, CustomChatRoomEventLogRepository {
}
