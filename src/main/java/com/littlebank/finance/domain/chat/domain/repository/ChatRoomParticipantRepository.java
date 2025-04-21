package com.littlebank.finance.domain.chat.domain.repository;

import com.littlebank.finance.domain.chat.domain.ChatRoomParticipant;
import com.littlebank.finance.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomParticipantRepository extends JpaRepository<ChatRoomParticipant, Long> {
    @Query("SELECT CASE WHEN COUNT(p)>0 THEN true ELSE false END " +
            "FROM ChatRoomParticipant p WHERE p.chatRoom.id=:roomId AND p.user.id=:userId")
    boolean existsByRoomIdAndUserId(@Param("roomId") String roomId,
                                    @Param("userId") String userId);
    void deleteAllByChatRoomId(String roomId);

    @Query("SELECT crp.user FROM ChatRoomParticipant crp WHERE crp.chatRoom.id = :roomId")
    List<User> findUsersByRoomId(@Param("roomId") String roomId);
}