package com.littlebank.finance.domain.subscription.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriptionPurchaseRequestDto {
    private String packageName;
    private String productId;
    private String purchaseToken;

//    public String getPackageName() {return packageName;}
//    public void setPackageName(String packageName) {this.packageName = packageName;}
//
//    public String getProductId() {return productId;}
//    public void setProductId(String productId) {this.productId = productId;}
//
//    public String getPurchaseToken() {return purchaseToken;}
//    public void setPurchaseToken(String purchaseToken) {this.purchaseToken = purchaseToken;}
}
