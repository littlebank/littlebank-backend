package com.littlebank.finance.domain.user.exception;

import com.littlebank.finance.global.error.exception.BusinessException;
import com.littlebank.finance.global.error.exception.ErrorCode;

public class AuthException extends BusinessException {
    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
