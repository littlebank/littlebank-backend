package com.littlebank.finance.domain.subscription.service;

import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.model.SubscriptionPurchaseV2;
import com.littlebank.finance.domain.subscription.dto.request.SubscriptionPurchaseRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final AndroidPublisher androidPublisher;
    public boolean verifyReceiptForGoogle(Long userId, SubscriptionPurchaseRequestDto request) {
        try {
            SubscriptionPurchaseV2 purchase = androidPublisher
                    .purchases()
                    .subscriptionsv2()
                    .get(request.getPackageName(), request.getPurchaseToken())
                    .execute();

            return  purchase.getAcknowledgementState() == "ACKNOWLEDGED"
                    && !purchase.getSubscriptionState().equals("CANCELED");
        } catch (Exception e) {
            return false;
        }
    }
}