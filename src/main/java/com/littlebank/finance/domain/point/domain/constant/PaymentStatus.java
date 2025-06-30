package com.littlebank.finance.domain.point.domain.constant;

import com.littlebank.finance.domain.point.exception.PointException;
import com.littlebank.finance.global.error.exception.ErrorCode;

public enum PaymentStatus {
    READY,       // 가상계좌 발급(입금 대기)
    PAID,        // 결제 완료
    FAILED,      // 결제 실패
    CANCELLED;   // 결제 취소

    public static PaymentStatus toPayMentStatus(String status) {
        return switch (status.toLowerCase()) {
            case "ready"     -> READY;
            case "paid"      -> PAID;
            case "cancelled" -> CANCELLED;
            case "failed"    -> FAILED;
            default -> throw new PointException(ErrorCode.PAYMENT_INVALID_STATUS);
        };
    }
}

