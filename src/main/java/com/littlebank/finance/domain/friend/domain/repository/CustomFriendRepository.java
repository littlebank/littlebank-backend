package com.littlebank.finance.domain.friend.domain.repository;

import com.littlebank.finance.domain.chat.dto.UserFriendInfoDto;
import com.littlebank.finance.domain.friend.dto.response.FriendInfoResponse;

import java.util.List;
import java.util.Optional;

public interface CustomFriendRepository {
    void updateCustomName(Long toUserId, String beforeName, String afterName);

    Optional<UserFriendInfoDto> findUserFriendInfoDto(Long fromUserId, Long toUserId);
    List<UserFriendInfoDto> findUserFriendInfoDtoList(Long fromUserId, List<Long> toUserIds);
    List<FriendInfoResponse> findFriendsByUserId(Long userId);
    List<FriendInfoResponse> searchFriendsByKeyword(Long userId, String keyword);
    List<FriendInfoResponse> findFriendAddedMeByUserId(Long userId);

}
