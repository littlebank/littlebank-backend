package com.littlebank.finance.domain.relationship.exception;

import com.littlebank.finance.global.error.exception.BusinessException;
import com.littlebank.finance.global.error.exception.ErrorCode;

public class RelationshipException extends BusinessException {
    public RelationshipException(ErrorCode errorCode) {
        super(errorCode);
    }
}
