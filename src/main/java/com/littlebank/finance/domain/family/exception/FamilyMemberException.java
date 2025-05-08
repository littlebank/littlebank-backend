package com.littlebank.finance.domain.family.exception;

import com.littlebank.finance.global.error.exception.BusinessException;
import com.littlebank.finance.global.error.exception.ErrorCode;

public class FamilyMemberException extends BusinessException {
    public FamilyMemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}
