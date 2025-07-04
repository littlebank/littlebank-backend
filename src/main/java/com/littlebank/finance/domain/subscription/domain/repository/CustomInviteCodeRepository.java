package com.littlebank.finance.domain.subscription.domain.repository;

import com.littlebank.finance.domain.subscription.dto.response.InviteCodeResponseDto;

import java.util.List;

public interface CustomInviteCodeRepository {
    List<InviteCodeResponseDto> findAllByOwnerId(Long ownerId);
}
