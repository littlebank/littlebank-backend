package com.littlebank.finance.domain.friend.service;

import com.littlebank.finance.domain.friend.domain.Friend;
import com.littlebank.finance.domain.friend.domain.repository.FriendRepository;
import com.littlebank.finance.domain.friend.dto.request.FriendAddRequest;
import com.littlebank.finance.domain.friend.dto.request.FriendBlockRequest;
import com.littlebank.finance.domain.friend.dto.request.FriendRenameRequest;
import com.littlebank.finance.domain.friend.dto.request.FriendUnblockRequest;
import com.littlebank.finance.domain.friend.dto.response.*;
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

    @Transactional(readOnly = true)
    public CustomPageResponse<FriendInfoResponse> getFriendAddedMe(Long userId, Pageable pageable) {
        return CustomPageResponse.of(friendRepository.findFriendAddedMeByUserId(userId, pageable));
    }

    public void deleteFriend(Long friendId) {
        Friend friend = friendRepository.findById(friendId)
                        .orElseThrow(() -> new FriendException(ErrorCode.FRIEND_NOT_FOUND));
        friendRepository.deleteById(friend.getId());
    }

    public BestFriendMarkResponse markBestFriend(Long friendId) {
        Friend friend = friendRepository.findByIdWithLock(friendId)
                .orElseThrow(() -> new FriendException(ErrorCode.FRIEND_NOT_FOUND));

        friend.markBestFriend();

        return BestFriendMarkResponse.of(friend);
    }

    public BestFriendMarkResponse unmarkBestFriend(Long friendId) {
        Friend friend = friendRepository.findByIdWithLock(friendId)
                .orElseThrow(() -> new FriendException(ErrorCode.FRIEND_NOT_FOUND));

        friend.unmarkBestFriend();

        return BestFriendMarkResponse.of(friend);
    }

    public FriendBlockStatusResponse blockFriend(Long userId, FriendBlockRequest request) {
        Friend friend = friendRepository.findByFromUserIdAndToUserIdWithLock(userId, request.getTargetUserId())
                .orElseGet(() -> {
                    verifyExistsFriend(userId, request.getTargetUserId());

                    User fromUser = userRepository.findById(userId)
                            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
                    User toUser = userRepository.findById(request.getTargetUserId())
                            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

                    return friendRepository.save(Friend.builder()
                            .fromUser(fromUser)
                            .toUser(toUser)
                            .customName(toUser.getName())
                            .isBlocked(true)
                            .build());
                });

        if (!friend.getIsBlocked()) {
            friend.blocking();
        }

        return FriendBlockStatusResponse.of(friend);
    }

    public FriendBlockStatusResponse unblockFriend(FriendUnblockRequest request) {
        Friend friend = friendRepository.findByIdWithLock(request.getFriendId())
                .orElseThrow(() -> new FriendException(ErrorCode.FRIEND_NOT_FOUND));

        friend.unblocking();

        return FriendBlockStatusResponse.of(friend);
    }

    public FriendRenameResponse renameFriend(Long userId, FriendRenameRequest request) {
        Friend friend = friendRepository.findById(request.getTargetFriendId())
                .orElseThrow(() -> new FriendException(ErrorCode.FRIEND_NOT_FOUND));
        verifyUserMatched(friend, userId);

        friend.rename(request.getChangeName());

        return FriendRenameResponse.of(friend);
    }

    private void verifyExistsFriend(Long fromUserId, Long toUserId) {
        if (friendRepository.existsByFromUserIdAndToUserId(fromUserId, toUserId)) {
            throw new FriendException(ErrorCode.ALREADY_FRIEND_EXISTS);
        }
    }

    private void verifyUserMatched(Friend friend, Long userId) {
        if (friend.getFromUser().getId() != userId) {
            throw new FriendException(ErrorCode.NO_PERMISSION_TO_MODIFY);
        }
    }
}
