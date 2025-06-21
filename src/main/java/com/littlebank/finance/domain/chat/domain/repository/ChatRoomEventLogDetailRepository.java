package com.littlebank.finance.domain.chat.domain.repository;

import com.littlebank.finance.domain.chat.domain.ChatRoomEventLogDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomEventLogDetailRepository extends JpaRepository<ChatRoomEventLogDetail, Long> {
}
