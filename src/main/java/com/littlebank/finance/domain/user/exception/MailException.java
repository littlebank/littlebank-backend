package com.littlebank.finance.domain.user.exception;

import com.littlebank.finance.global.error.exception.BusinessException;
import com.littlebank.finance.global.error.exception.ErrorCode;

public class MailException extends BusinessException {
    public MailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
