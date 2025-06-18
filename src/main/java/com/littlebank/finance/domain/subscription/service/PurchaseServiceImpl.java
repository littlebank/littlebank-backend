package com.littlebank.finance.domain.subscription.service;

import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.model.SubscriptionPurchaseV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {
    private final AndroidPublisher androidPublisher;

    @Override
    public SubscriptionPurchaseV2 verifyReceiptForGoogle(String purchaseToken) throws IOException {
        AndroidPublisher.Purchases.Subscriptionsv2.Get request =
                androidPublisher.purchases().subscriptionsv2().get("com.littlebank.app", purchaseToken);
        return request.execute();

    }
    }
