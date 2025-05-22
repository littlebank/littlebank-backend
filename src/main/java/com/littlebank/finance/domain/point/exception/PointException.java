package com.littlebank.finance.domain.point.exception;

import com.littlebank.finance.global.error.exception.BusinessException;
import com.littlebank.finance.global.error.exception.ErrorCode;

public class PointException extends BusinessException {
    public PointException(ErrorCode errorCode) {
        super(errorCode);
    }
}
