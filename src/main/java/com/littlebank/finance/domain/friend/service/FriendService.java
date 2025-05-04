package com.littlebank.finance.domain.friend.service;


import com.littlebank.finance.domain.friend.domain.Friend;
import com.littlebank.finance.domain.friend.domain.repository.FriendRepository;
import com.littlebank.finance.domain.friend.dto.request.FriendAddRequest;
import com.littlebank.finance.domain.friend.dto.response.FriendAddResponse;
import com.littlebank.finance.domain.friend.dto.response.FriendInfoResponse;
import com.littlebank.finance.domain.friend.exception.FriendException;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.common.CustomPageResponse;
import com.littlebank.finance.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendService {
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    public FriendAddResponse addFriend(FriendAddRequest request, Long userId) {
        verifyExistsFriend(userId, request.getTargetUserId());

        User fromUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        User toUser = userRepository.findById(request.getTargetUserId())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        Friend friend = friendRepository.save(
                Friend.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .customName(toUser.getName())
                .build()
        );

        return FriendAddResponse.of(friend);
    }

    @Transactional(readOnly = true)
    public CustomPageResponse<FriendInfoResponse> getFriendList(Long userId, Pageable pageable) {
        return CustomPageResponse.of(friendRepository.findFriendsByUserId(userId, pageable));
    }

    public void deleteFriend(Long friendId) {
        Friend friend = friendRepository.findById(friendId)
                        .orElseThrow(() -> new FriendException(ErrorCode.FRIEND_NOT_FOUND));
        friendRepository.deleteById(friend.getId());
    }

    private void verifyExistsFriend(Long fromUserId, Long toUserId) {
        if (friendRepository.existsByFromUserIdAndToUserId(fromUserId, toUserId)) {
            throw new FriendException(ErrorCode.ALREADY_FRIEND_EXISTS);
        }
    }
}
