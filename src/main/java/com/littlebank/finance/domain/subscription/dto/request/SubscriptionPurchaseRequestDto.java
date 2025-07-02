package com.littlebank.finance.domain.subscription.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriptionPurchaseRequestDto {
    @NotNull
    @Schema(description = "구글 패키지명")
    private String packageName;
    @NotNull
    @Schema(description = "구매 상품 아이디")
    private String productId;
    @NotNull
    @Schema(description = "구매 토큰")
    private String purchaseToken;
    @Schema(description = "구매자 구독 자리에 포함 유무")
    private boolean includeOwner;
}
