package com.littlebank.finance.global.portone.exception;

import com.littlebank.finance.global.error.exception.BusinessException;
import com.littlebank.finance.global.error.exception.ErrorCode;

public class PortoneException extends BusinessException {
    public PortoneException(ErrorCode errorCode) {
        super(errorCode);
    }
}
