package com.littlebank.finance.domain.chat.domain.repository;

import com.littlebank.finance.domain.chat.domain.UserChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long>, CustomUserChatRoomRepository {
    List<UserChatRoom> findAllByRoomId(Long roomId);
}
