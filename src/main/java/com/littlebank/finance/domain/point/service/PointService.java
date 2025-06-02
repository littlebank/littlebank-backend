package com.littlebank.finance.domain.point.service;

import com.littlebank.finance.global.portone.PortoneService;
import com.littlebank.finance.domain.point.domain.Payment;
import com.littlebank.finance.domain.point.domain.PaymentStatus;
import com.littlebank.finance.domain.point.domain.repository.PaymentRepository;
import com.littlebank.finance.global.portone.dto.PortonePaymentDto;
import com.littlebank.finance.domain.point.dto.request.PaymentInfoSaveRequest;
import com.littlebank.finance.domain.point.dto.response.PaymentHistoryResponse;
import com.littlebank.finance.domain.point.dto.response.PaymentInfoSaveResponse;
import com.littlebank.finance.domain.point.exception.PointException;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.common.CustomPageResponse;
import com.littlebank.finance.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class PointService {
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final PortoneService portoneService;

    public PaymentInfoSaveResponse verifyAndSave(Long userId, PaymentInfoSaveRequest request) {
        String token = portoneService.getAccessToken();
        PortonePaymentDto paymentDto = portoneService.getPaymentInfo(request.getImpUid(), token);

        if (paymentDto.getStatus() != PaymentStatus.PAID) {
            throw new PointException(ErrorCode.PAYMENT_STATUS_NOT_PAID);
        }

        if (paymentRepository.existsByImpUid(request.getImpUid())) {
            throw new PointException(ErrorCode.PAYMENT_ALREADY_EXISTS); // 중복 방지
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        Payment payment = paymentRepository.save(Payment.builder()
                .impUid(paymentDto.getImpUid())
                .merchantUid(paymentDto.getMerchantUid())
                .amount(paymentDto.getAmount())
                .payMethod(paymentDto.getPayMethod())
                .pgProvider(paymentDto.getPgProvider())
                .status(paymentDto.getStatus())
                .paidAt(LocalDateTime.now())
                .user(user)
                .isDeleted(Boolean.FALSE)
                .build());

        user.addPoint(payment.getAmount());

        payment.recordRemainingPoint(user);

        return PaymentInfoSaveResponse.of(payment);
    }

    @Transactional(readOnly = true)
    public CustomPageResponse<PaymentHistoryResponse> getPaymentHistory(Long userId, Pageable pageable) {
        return CustomPageResponse.of(paymentRepository.findHistoryByUserId(userId, pageable));
    }


}
