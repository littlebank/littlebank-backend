package com.littlebank.finance.domain.friend.domain.repository;

import com.littlebank.finance.domain.chat.dto.UserFriendInfoDto;
import com.littlebank.finance.domain.friend.dto.response.FriendInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface CustomFriendRepository {
    Optional<UserFriendInfoDto> findUserFriendInfoDto(Long fromUserId, Long toUserId);
    List<UserFriendInfoDto> findUserFriendInfoDtoList(Long fromUserId, List<Long> toUserIds);
    List<FriendInfoResponse> findFriendsByUserId(Long userId);
    Page<FriendInfoResponse> findFriendsByUserId(Long userId, Pageable pageable);
    Page<FriendInfoResponse> findFriendAddedMeByUserId(Long userId, Pageable pageable);

}
