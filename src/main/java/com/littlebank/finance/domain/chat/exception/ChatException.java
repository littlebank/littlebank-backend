package com.littlebank.finance.domain.chat.exception;

import com.littlebank.finance.global.error.exception.BusinessException;
import com.littlebank.finance.global.error.exception.ErrorCode;

public class ChatException extends BusinessException {
    public ChatException(ErrorCode errorCode) {
        super(errorCode);
    }
}
