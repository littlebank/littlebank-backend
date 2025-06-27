package com.littlebank.finance.domain.family.exception;

import com.littlebank.finance.global.error.exception.BusinessException;
import com.littlebank.finance.global.error.exception.ErrorCode;

public class FamilyException extends BusinessException {
    public FamilyException(ErrorCode errorCode) {
        super(errorCode);
    }
}
