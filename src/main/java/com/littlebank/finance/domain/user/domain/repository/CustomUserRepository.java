package com.littlebank.finance.domain.user.domain.repository;

import com.littlebank.finance.domain.user.dto.response.UserDetailsInfoResponse;
import com.littlebank.finance.domain.user.dto.response.UserSearchResponse;

import java.util.Optional;

public interface CustomUserRepository {
    Optional<UserDetailsInfoResponse> findUserDetailsInfo(Long searchUserId, Long userId);
    Optional<UserSearchResponse> findUserSearchResponse(Long requesterId, String phone);
}
