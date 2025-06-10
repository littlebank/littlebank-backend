package com.littlebank.finance.domain.subscription.controller;

import com.littlebank.finance.domain.subscription.domain.Subscription;
import com.littlebank.finance.domain.subscription.dto.request.SubscriptionCreateRequestDto;
import com.littlebank.finance.domain.subscription.dto.response.SubscriptionResponseDto;
import com.littlebank.finance.domain.subscription.service.SubscriptionService;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api-user/subscription")
@RequiredArgsConstructor
@Tag(name = "Subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Operation(summary = "구독 생성")
    @PostMapping("/create")
    public ResponseEntity<SubscriptionResponseDto> createSubscription(
            @RequestBody SubscriptionCreateRequestDto request,
            @AuthenticationPrincipal CustomUserDetails user
            ) {
        SubscriptionResponseDto response = subscriptionService.createSubscription(user.getId(), request);
        return ResponseEntity.ok(response);
    }

}
