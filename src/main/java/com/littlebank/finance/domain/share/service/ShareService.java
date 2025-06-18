package com.littlebank.finance.domain.share.service;

import com.littlebank.finance.domain.share.dto.request.CreateLinkRequestDto;
import com.littlebank.finance.domain.share.dto.response.CreateLinkResponseDto;
import com.littlebank.finance.domain.share.exception.ShareException;
import com.littlebank.finance.domain.subscription.domain.InviteCode;
import com.littlebank.finance.domain.subscription.domain.Subscription;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShareService {

    private final UserRepository userRepository;

    @Value("${app.invite.base-url}")
    private String inviteBaseUrl;


    public CreateLinkResponseDto createLink(Long userId, CreateLinkRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        Subscription subscription = user.getSubscription();

        List<InviteCode> availableCodes = subscription.getInviteCodes().stream()
                .filter(code -> !code.isUsed())
                .toList();
        if (availableCodes.size() < request.getCount()) {
            throw new ShareException(ErrorCode.EXCEEDED_SUBSCRIPTUIN_SEATS);
        }
        List<String> links = new ArrayList<>();
        for (int i = 0; i < request.getCount(); i++) {
            InviteCode inviteCode = availableCodes.get(i);
            String link = inviteBaseUrl + inviteCode.getCode();
            links.add(link);
        }

        return new CreateLinkResponseDto(links);
    }
}
