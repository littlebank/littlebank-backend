package com.littlebank.finance.domain.challenge.exception;

import com.littlebank.finance.global.error.exception.BusinessException;
import com.littlebank.finance.global.error.exception.ErrorCode;

public class ChallengeException extends BusinessException {
    public ChallengeException(ErrorCode errorCode) {super(errorCode);}
}
