package com.littlebank.finance.domain.goal.exception;

import com.littlebank.finance.global.error.exception.BusinessException;
import com.littlebank.finance.global.error.exception.ErrorCode;

public class GoalException extends BusinessException {
    public GoalException(ErrorCode errorCode) {
        super(errorCode);
    }
}
