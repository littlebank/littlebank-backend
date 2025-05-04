package com.littlebank.finance.domain.friend.domain.repository;

import com.littlebank.finance.domain.friend.domain.Friend;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long>, CustomFriendRepository {
    boolean existsByFromUserIdAndToUserId(Long fromUserId, Long toUserId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select f from Friend f where f.id = :id")
    Optional<Friend> findByIdWithLock(@Param("id") Long friendId);

}
