package com.littlebank.finance.domain.friend.exception;

import com.littlebank.finance.global.error.exception.BusinessException;
import com.littlebank.finance.global.error.exception.ErrorCode;

public class FriendException extends BusinessException {
    public FriendException(ErrorCode errorCode) {
        super(errorCode);
    }
}
