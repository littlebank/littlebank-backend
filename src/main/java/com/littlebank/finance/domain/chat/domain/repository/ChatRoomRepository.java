package com.littlebank.finance.domain.chat.domain.repository;

import com.littlebank.finance.domain.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

}