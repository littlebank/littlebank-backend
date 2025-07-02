package com.littlebank.finance.domain.subscription.service;

import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.model.SubscriptionPurchaseLineItem;
import com.google.api.services.androidpublisher.model.SubscriptionPurchaseV2;
import com.littlebank.finance.domain.subscription.domain.Subscription;
import com.littlebank.finance.domain.subscription.dto.request.SubscriptionCreateRequestDto;
import com.littlebank.finance.domain.subscription.dto.request.SubscriptionPurchaseRequestDto;
import com.littlebank.finance.domain.subscription.dto.response.SubscriptionResponseDto;
import com.littlebank.finance.domain.subscription.dto.response.SubscriptionStatusResponseDto;
import com.littlebank.finance.domain.subscription.exception.SubscriptionException;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final AndroidPublisher androidPublisher;
    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;

    public SubscriptionResponseDto verifySubscription(Long userId, SubscriptionPurchaseRequestDto request) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

            SubscriptionPurchaseV2 purchase = androidPublisher
                    .purchases()
                    .subscriptionsv2()
                    .get(request.getPackageName(), request.getPurchaseToken())
                    .execute();

            boolean isAcknowledged = "ACKNOWLEDGED".equals(purchase.getAcknowledgementState());
            boolean isActive = !"CANCELED".equals(purchase.getSubscriptionState());
            if (!isAcknowledged || !isActive) {
                throw new SubscriptionException(ErrorCode.GOOGLE_TOKEN_NOT_FOUND);
            }

            List<SubscriptionPurchaseLineItem> items = purchase.getLineItems();
            if (items == null || items.isEmpty()) {
                throw new SubscriptionException(ErrorCode.GOOGLE_ITEM_NOT_FOUND);
            }

            String startTimeStr = purchase.getStartTime();
            String expiryTimeStr = items.get(0).getExpiryTime();

            LocalDateTime startDate = ZonedDateTime.parse(startTimeStr, DateTimeFormatter.ISO_ZONED_DATE_TIME)
                    .withZoneSameInstant(ZoneId.systemDefault())
                    .toLocalDateTime();
            LocalDateTime endDate = ZonedDateTime.parse(expiryTimeStr, DateTimeFormatter.ISO_ZONED_DATE_TIME)
                    .withZoneSameInstant(ZoneId.systemDefault())
                    .toLocalDateTime();
            int seat = switch (request.getProductId()) {
                case "one_month_ly" -> 1;
                case "three_month_ly" -> 3;
                case "five_month_ly" -> 5;
                default -> throw new SubscriptionException(ErrorCode.INVALID_INPUT_VALUE);
            };
            boolean includeOwner = (seat == 1) || request.isIncludeOwner();
            SubscriptionCreateRequestDto createRequest = SubscriptionCreateRequestDto.builder()
                    .seat(seat)
                    .includeOwner(includeOwner)
                    .startDate(startDate)
                    .endDate(endDate)
                    .build();

            return subscriptionService.createSubscription(userId, createRequest);

        } catch (Exception e) {
            throw new SubscriptionException(ErrorCode.GOOGLE_VERIFICATION_FAIL);
        }
    }

    public SubscriptionStatusResponseDto getSubscriptionStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        Subscription subscription = user.getSubscription();
        if (subscription == null) {
            return SubscriptionStatusResponseDto.builder()
                    .active(false)
                    .endDate(null)
                    .autoRenew(false)
                    .build();
        }
        boolean isActive = subscription.getEndDate().isAfter(LocalDateTime.now());
        boolean autoRenew = false;
        try {
            SubscriptionPurchaseV2 purchase = androidPublisher
                    .purchases()
                    .subscriptionsv2()
                    .get("com.littlebank.littlebank", subscription.getPurchaseToken())
                    .execute();

            autoRenew = purchase.getLineItems().get(0).getAutoRenewingPlan().getAutoRenewEnabled();
        } catch (Exception e) {
            throw new SubscriptionException(ErrorCode.GOOGLE_TOKEN_NOT_FOUND);
        }

        return SubscriptionStatusResponseDto.builder()
                .active(isActive)
                .endDate(subscription.getEndDate())
                .autoRenew(autoRenew)
                .build();
    }
}