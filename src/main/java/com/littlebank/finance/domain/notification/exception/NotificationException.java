package com.littlebank.finance.domain.notification.exception;

import com.littlebank.finance.global.error.exception.BusinessException;
import com.littlebank.finance.global.error.exception.ErrorCode;

public class NotificationException extends BusinessException {
    public NotificationException(ErrorCode errorCode) {super(errorCode);}
}
