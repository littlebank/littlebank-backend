package com.littlebank.finance.domain.subscription.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriptionPurchaseRequestDto {
    private String packageName;
    private String productId;
    private String purchaseToken;
}
