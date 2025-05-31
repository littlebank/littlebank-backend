package com.littlebank.finance.domain.mission.exception;

import com.littlebank.finance.global.error.exception.BusinessException;
import com.littlebank.finance.global.error.exception.ErrorCode;

public class MissionException extends BusinessException {
    public MissionException(ErrorCode errorCode) {
        super(errorCode);
    }
}
