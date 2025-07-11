package com.littlebank.finance.domain.school.exception;

import com.littlebank.finance.global.error.exception.BusinessException;
import com.littlebank.finance.global.error.exception.ErrorCode;

public class SchoolException extends BusinessException {
  public SchoolException(ErrorCode errorCode) {
    super(errorCode);
  }
}
