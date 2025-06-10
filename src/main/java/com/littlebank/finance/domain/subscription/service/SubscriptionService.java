package com.littlebank.finance.domain.subscription.service;

import com.littlebank.finance.domain.subscription.domain.InviteCode;
import com.littlebank.finance.domain.subscription.domain.Subscription;
import com.littlebank.finance.domain.subscription.domain.repository.InviteCodeRepository;
import com.littlebank.finance.domain.subscription.domain.repository.SubscriptionRepository;
import com.littlebank.finance.domain.subscription.dto.request.SubscriptionCreateRequestDto;
import com.littlebank.finance.domain.subscription.dto.response.SubscriptionResponseDto;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class SubscriptionService {
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final InviteCodeRepository inviteCodeRepository;
    public SubscriptionResponseDto createSubscription(Long userId, SubscriptionCreateRequestDto request) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusMonths(1);

        Subscription subscription = Subscription.builder()
                .owner(owner)
                .seat(request.getSeat())
                .startDate(startDate)
                .endDate(endDate)
                .build();
        subscription = subscriptionRepository.save(subscription);

        owner.setSubscription(subscription);
        userRepository.save(owner);

        for (int i=0; i< request.getSeat() -1; i++) {
            String code = UUID.randomUUID().toString().replace("-","").substring(0,12);
            InviteCode inviteCode = InviteCode.builder()
                    .code(code)
                    .used(false)
                    .subscription(subscription)
                    .build();
            inviteCodeRepository.save(inviteCode);
            subscription.addInviteCode(inviteCode);
        }

        return SubscriptionResponseDto.of(subscription);
    }

//    public SubscriptionResponseDto getMySubscription(Long userId) {
//
//    }
}
