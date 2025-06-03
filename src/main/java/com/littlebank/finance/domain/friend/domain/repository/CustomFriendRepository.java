package com.littlebank.finance.domain.friend.domain.repository;

import com.littlebank.finance.domain.friend.dto.response.FriendInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CustomFriendRepository {
    Page<FriendInfoResponse> findFriendsByUserId(Long userId, Pageable pageable);
    Page<FriendInfoResponse> findFriendAddedMeByUserId(Long userId, Pageable pageable);
    List<FriendInfoResponse> findFriendsByUserId(Long userId);
}
