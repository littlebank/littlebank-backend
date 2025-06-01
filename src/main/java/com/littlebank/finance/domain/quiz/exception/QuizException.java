package com.littlebank.finance.domain.quiz.exception;

import com.littlebank.finance.global.error.exception.BusinessException;
import com.littlebank.finance.global.error.exception.ErrorCode;

public class QuizException extends BusinessException {
    public QuizException(ErrorCode errorCode ) {
        super(errorCode);
    }
}
