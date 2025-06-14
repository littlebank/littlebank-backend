package com.littlebank.finance.domain.chat.domain.repository;

import com.littlebank.finance.domain.chat.domain.UserChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long>, CustomUserChatRoomRepository {
    Optional<UserChatRoom> findByUserIdAndRoomId(Long userId, Long roomId);
}
