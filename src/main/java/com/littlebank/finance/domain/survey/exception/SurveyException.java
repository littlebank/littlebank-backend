package com.littlebank.finance.domain.survey.exception;

import com.littlebank.finance.global.error.exception.BusinessException;
import com.littlebank.finance.global.error.exception.ErrorCode;

public class SurveyException extends BusinessException {
    public SurveyException(ErrorCode errorCode ) {
        super(errorCode);
    }
}
