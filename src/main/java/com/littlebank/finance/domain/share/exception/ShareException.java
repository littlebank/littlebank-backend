package com.littlebank.finance.domain.share.exception;

import com.littlebank.finance.global.error.exception.BusinessException;
import com.littlebank.finance.global.error.exception.ErrorCode;

public class ShareException extends BusinessException {
    public ShareException(ErrorCode errorCode) {super(errorCode);}
}
