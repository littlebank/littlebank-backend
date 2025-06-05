package com.littlebank.finance.domain.point.service;

import com.littlebank.finance.domain.point.domain.*;
import com.littlebank.finance.domain.point.domain.repository.PaymentRepository;
import com.littlebank.finance.domain.point.domain.repository.RefundRepository;
import com.littlebank.finance.domain.point.domain.repository.TransactionHistoryRepository;
import com.littlebank.finance.domain.point.dto.request.PaymentInfoSaveRequest;
import com.littlebank.finance.domain.point.dto.request.PointRefundRequest;
import com.littlebank.finance.domain.point.dto.request.PointTransferRequest;
import com.littlebank.finance.domain.point.dto.response.*;
import com.littlebank.finance.domain.point.exception.PointException;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.UserRole;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.business.PointPolicy;
import com.littlebank.finance.global.common.CustomPageResponse;
import com.littlebank.finance.global.error.exception.ErrorCode;
import com.littlebank.finance.global.portone.PortoneService;
import com.littlebank.finance.global.portone.dto.PortonePaymentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PointService {
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final RefundRepository refundRepository;
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
                .build());

        user.addPoint(payment.getAmount());

        payment.recordRemainingPoint(user);

        return PaymentInfoSaveResponse.of(payment);
    }

    @Transactional(readOnly = true)
    public CustomPageResponse<PaymentHistoryResponse> getPaymentHistory(Long userId, Pageable pageable) {
        return CustomPageResponse.of(paymentRepository.findHistoryByUserId(userId, pageable));
    }

    public CommonPointTransferResponse transferPoint(Long userId, PointTransferRequest request) {
        User sender = userRepository.findByIdWithLock(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        if (sender.getPoint() < request.getPointAmount()) {
            throw new PointException(ErrorCode.INSUFFICIENT_POINT_BALANCE);
        }

        User receiver = userRepository.findByIdWithLock(request.getReceiverId())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        sender.sendPoint(request.getPointAmount());
        receiver.receivePoint(request.getPointAmount());

        TransactionHistory transactionHistory = transactionHistoryRepository.save(
                TransactionHistory.builder()
                        .pointAmount(request.getPointAmount())
                        .message(request.getMessage())
                        .senderRemainingPoint(sender.getPoint())
                        .receiverRemainingPoint(receiver.getPoint())
                        .sender(sender)
                        .receiver(receiver)
                        .build()
        );

        return CommonPointTransferResponse.of(transactionHistory);
    }

    @Transactional(readOnly = true)
    public CustomPageResponse<ReceivePointHistoryResponse> getReceivedPointHistory(Long userId, Pageable pageable) {
        return CustomPageResponse.of(transactionHistoryRepository.findReceivedPointHistoryByUserId(userId, pageable));
    }

    @Transactional(readOnly = true)
    public CustomPageResponse<SendPointHistoryResponse> getSentPointHistory(Long userId, Pageable pageable) {
        List<SendPointHistoryResponse> sendResults = transactionHistoryRepository.findSentPointHistoryByUserId(userId);
        List<SendPointHistoryResponse> refundResults = refundRepository.findRefundHistoryByUserId(userId);

        List<SendPointHistoryResponse> merged = new ArrayList<>();
        merged.addAll(sendResults);
        merged.addAll(refundResults);

        merged.sort((a, b) -> b.getSentAt().compareTo(a.getSentAt()));

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), merged.size());
        List<SendPointHistoryResponse> pageContent = merged.subList(start, end);

        return CustomPageResponse.of(new PageImpl<>(pageContent, pageable, merged.size()));
    }

    @Transactional(readOnly = true)
    public CustomPageResponse<LatestSentAccountResponse> getLatestSentAccount(Long userId, Pageable pageable) {
        return CustomPageResponse.of(transactionHistoryRepository.findLatestSentAccountByUserId(userId, pageable));
    }

    public PointRefundResponse refundPoint(Long userId, PointRefundRequest request) {
        User user = userRepository.findByIdWithLock(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        if (user.getPoint() < request.getExchangeAmount()) {
            throw new PointException(ErrorCode.INSUFFICIENT_POINT_BALANCE);
        }

        user.exchangePointToMoney(request.getExchangeAmount());

        int processedAmount = request.getExchangeAmount();
        if (user.getRole() == UserRole.CHILD &&
                request.getExchangeAmount() < PointPolicy.EXCHANGE_FEE_EXEMPTION_AMOUNT) {
            processedAmount -= PointPolicy.CHILD_COMMISSION;
        }

        Refund refund = refundRepository.save(
                Refund.builder()
                        .requestedAmount(request.getExchangeAmount())
                        .processedAmount(processedAmount)
                        .remainingPoint(user.getPoint())
                        .status(RefundStatus.WAIT)
                        .user(user)
                        .build()
        );

        return PointRefundResponse.of(refund);
    }
}
