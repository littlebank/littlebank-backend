package com.littlebank.finance.domain.subscription.service;

import com.google.api.services.androidpublisher.model.SubscriptionPurchaseV2;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface PurchaseService {
    SubscriptionPurchaseV2 verifyReceiptForGoogle(String purchaseToken) throws IOException, GeneralSecurityException;
}
