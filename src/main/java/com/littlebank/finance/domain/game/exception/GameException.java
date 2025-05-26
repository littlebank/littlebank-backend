package com.littlebank.finance.domain.game.exception;

import com.littlebank.finance.global.error.exception.BusinessException;
import com.littlebank.finance.global.error.exception.ErrorCode;

public class GameException extends BusinessException {
    public GameException(ErrorCode errorCode) {
        super(errorCode);
    }
}
