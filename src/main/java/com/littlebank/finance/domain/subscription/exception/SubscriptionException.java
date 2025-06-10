package com.littlebank.finance.domain.subscription.exception;

import com.littlebank.finance.global.error.exception.BusinessException;
import com.littlebank.finance.global.error.exception.ErrorCode;

public class SubscriptionException extends BusinessException {
    public SubscriptionException(ErrorCode errorCode ) {
        super(errorCode);
    }
}
