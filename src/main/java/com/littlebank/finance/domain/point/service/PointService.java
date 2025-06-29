package com.littlebank.finance.domain.point.service;

import com.littlebank.finance.domain.challenge.domain.ChallengeParticipation;
import com.littlebank.finance.domain.challenge.domain.repository.ChallengeParticipationRepository;
import com.littlebank.finance.domain.challenge.exception.ChallengeException;
import com.littlebank.finance.domain.goal.domain.Goal;
import com.littlebank.finance.domain.goal.domain.repository.GoalRepository;
import com.littlebank.finance.domain.goal.exception.GoalException;
import com.littlebank.finance.domain.mission.domain.Mission;
import com.littlebank.finance.domain.mission.domain.repository.MissionRepository;
import com.littlebank.finance.domain.mission.exception.MissionException;
import com.littlebank.finance.domain.notification.domain.Notification;
import com.littlebank.finance.domain.notification.domain.NotificationType;
import com.littlebank.finance.domain.notification.domain.repository.NotificationRepository;
import com.littlebank.finance.domain.point.domain.*;
import com.littlebank.finance.domain.point.domain.repository.PaymentRepository;
import com.littlebank.finance.domain.point.domain.repository.RefundRepository;
import com.littlebank.finance.domain.point.domain.repository.TransactionHistoryRepository;
import com.littlebank.finance.domain.point.dto.request.ChildPointRefundRequest;
import com.littlebank.finance.domain.point.dto.request.PaymentInfoSaveRequest;
import com.littlebank.finance.domain.point.dto.request.ParentPointRefundRequest;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PointService {
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final RefundRepository refundRepository;
    private final PortoneService portoneService;
    private final GoalRepository goalRepository;
    private final MissionRepository missionRepository;
    private final ChallengeParticipationRepository challengeParticipationRepository;
    private final NotificationRepository notificationRepository;
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
        validateNotRewarded(request.getRewardType(), request.getRewardId());
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
                        .rewardType(request.getRewardType())
                        .rewardId(request.getRewardId())
                        .build()
        );
        // receiver한테 알림 가도록
        try {
            Notification receiverNotification = Notification.builder()
                    .receiver(receiver)
                    .message(sender.getName() + "님에게" + request.getPointAmount() + "포인트를 받았습니다!")
                    .subMessage("앱에 들어가서 확인해보세요!")
                    .type(NotificationType.POINT_TRANSFER)
                    .targetId(transactionHistory.getId())
                    .isRead(false)
                    .build();
            notificationRepository.save(receiverNotification);

            Notification senderNotification = Notification.builder()
                    .receiver(sender)
                    .message(receiver.getName() + "님에게" + request.getPointAmount() + "포인트를 보냈습니다!")
                    .type(NotificationType.POINT_TRANSFER)
                    .targetId(transactionHistory.getId())
                    .isRead(false)
                    .build();
            notificationRepository.save(senderNotification);
        } catch (DataIntegrityViolationException e) {
            log.warn("이미 동일한 알림이 존재합니다.");
        }
        return CommonPointTransferResponse.of(transactionHistory);
    }

    private void validateNotRewarded(RewardType type, Long rewardId) {
        switch (type) {
            case GOAL -> {
                Goal goal = goalRepository.findById(rewardId)
                        .orElseThrow(() -> new GoalException(ErrorCode.GOAL_NOT_FOUND));
                if (goal.getIsRewarded()) {
                    throw new PointException(ErrorCode.ALREADY_REWARDED);
                }
                goal.rewarded();
            }
            case MISSION -> {
                Mission mission = missionRepository.findById(rewardId)
                        .orElseThrow(() -> new MissionException(ErrorCode.MISSION_NOT_FOUND));
                if (mission.getIsRewarded()) {
                    throw new PointException(ErrorCode.ALREADY_REWARDED);
                }
                mission.rewarded();
            }
            case CHALLENGE -> {
                ChallengeParticipation participation = challengeParticipationRepository.findById(rewardId)
                        .orElseThrow(() -> new ChallengeException(ErrorCode.CHALLENGE_NOT_FOUND));
                if (participation.getIsRewarded()) {
                    throw new PointException(ErrorCode.ALREADY_REWARDED);
                }
                participation.rewarded();
            }
            default -> throw new PointException(ErrorCode.INVALID_REWARD_TYPE);
        }
    }
    @Transactional(readOnly = true)
    public CustomPageResponse<ReceivePointHistoryResponse> getReceivedPointHistory(Long userId, Pageable pageable) {
        List<ReceivePointHistoryResponse> sendResults = transactionHistoryRepository.findReceivedPointHistoryByUserId(userId);
        List<ReceivePointHistoryResponse> paymentResults = paymentRepository.findPaymentHistoryByUserId(userId);

        Map<RewardType, List<Long>> rewardIdMap = sendResults.stream()
                .filter(r -> r.getRewardType() != null && r.getRewardId() != null)
                .collect(Collectors.groupingBy(
                        ReceivePointHistoryResponse::getRewardType,
                        Collectors.mapping(ReceivePointHistoryResponse::getRewardId, Collectors.toList())
                ));
        Map<Long, String> goalTitleMap = goalRepository.findIdTitleMapByIds(
                rewardIdMap.getOrDefault(RewardType.GOAL, List.of())
        );
        Map<Long, String> missionTitleMap = missionRepository.findIdTitleMapByIds(
                rewardIdMap.getOrDefault(RewardType.MISSION, List.of())
        );
        Map<Long, String> challengeTitleMap = challengeParticipationRepository.findIdTitleMapByIds(
                rewardIdMap.getOrDefault(RewardType.CHALLENGE, List.of())
        );

        sendResults.forEach(r -> {
            if (r.getRewardType() == null || r.getRewardId() == null) return;
            switch (r.getRewardType()) {
                case GOAL -> r.setRewardTitle(goalTitleMap.getOrDefault(r.getRewardId(), "알 수 없음"));
                case MISSION -> r.setRewardTitle(missionTitleMap.getOrDefault(r.getRewardId(), "알 수 없음"));
                case CHALLENGE -> r.setRewardTitle(challengeTitleMap.getOrDefault(r.getRewardId(), "알 수 없음"));
                default -> r.setRewardTitle("일반 포인트");
            }
        });

        List<ReceivePointHistoryResponse> merged = new ArrayList<>();
        merged.addAll(sendResults);
        merged.addAll(paymentResults);

        merged.sort((a, b) -> b.getReceivedAt().compareTo(a.getReceivedAt()));

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), merged.size());
        List<ReceivePointHistoryResponse> pageContent = merged.subList(start, end);

        return CustomPageResponse.of(new PageImpl<>(pageContent, pageable, merged.size()));
    }

    @Transactional(readOnly = true)
    public CustomPageResponse<SendPointHistoryResponse> getSentPointHistory(Long userId, Pageable pageable) {
        List<SendPointHistoryResponse> sendResults = transactionHistoryRepository.findSentPointHistoryByUserId(userId);
        List<SendPointHistoryResponse> refundResults = refundRepository.findRefundHistoryByUserId(userId);

        Map<RewardType, List<Long>> rewardIdMap = sendResults.stream()
                .filter(r -> r.getRewardType() != null && r.getRewardId() != null)
                .collect(Collectors.groupingBy(
                        SendPointHistoryResponse::getRewardType,
                        Collectors.mapping(SendPointHistoryResponse::getRewardId, Collectors.toList())
                ));

        Map<Long, String> goalTitleMap = goalRepository.findIdTitleMapByIds(rewardIdMap.getOrDefault(RewardType.GOAL, List.of()));
        Map<Long, String> missionTitleMap = missionRepository.findIdTitleMapByIds(rewardIdMap.getOrDefault(RewardType.MISSION, List.of()));
        Map<Long, String> challengeTitleMap = challengeParticipationRepository.findIdTitleMapByIds(rewardIdMap.getOrDefault(RewardType.CHALLENGE, List.of()));

        sendResults.forEach(r -> {
            if (r.getRewardType() == null || r.getRewardId() == null) return;
            switch (r.getRewardType()) {
                case GOAL -> r.setRewardTitle(goalTitleMap.getOrDefault(r.getRewardId(), "알 수 없음"));
                case MISSION -> r.setRewardTitle(missionTitleMap.getOrDefault(r.getRewardId(), "알 수 없음"));
                case CHALLENGE -> r.setRewardTitle(challengeTitleMap.getOrDefault(r.getRewardId(), "알 수 없음"));
                default -> r.setRewardTitle("일반 포인트");
            }
        });

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

    public PointRefundResponse refundPointByParent(Long userId, ParentPointRefundRequest request) {
        User user = userRepository.findByIdWithLock(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        if (user.getRole() != UserRole.PARENT) {
            throw new UserException(ErrorCode.FORBIDDEN_PARENT_ONLY);
        }
        if (user.getBankName() == null || user.getBankAccount() == null) {
            throw new UserException(ErrorCode.EXCHANGE_ACCOUNT_NOT_REGISTERED);
        }
        if (user.getPoint() < request.getExchangeAmount()) {
            throw new PointException(ErrorCode.INSUFFICIENT_POINT_BALANCE);
        }


        user.exchangePointToMoney(request.getExchangeAmount());

        int processedAmount = request.getExchangeAmount();

        Refund refund = refundRepository.save(
                Refund.builder()
                        .requestedAmount(request.getExchangeAmount())
                        .processedAmount(processedAmount)
                        .remainingPoint(user.getPoint())
                        .status(RefundStatus.WAIT)
                        .user(user)
                        .depositTargetUser(user)
                        .build()
        );

        try {
            Notification notification = Notification.builder()
                    .receiver(user)
                    .message(processedAmount + "포인트를 꺼냈습니다!")
                    .subMessage("앱에 들어가서 확인해보세요!")
                    .type(NotificationType.POINT_REFUND)
                    .targetId(refund.getId())
                    .isRead(false)
                    .build();
            notificationRepository.save(notification);
        } catch (DataIntegrityViolationException e) {
            log.warn("이미 동일한 알림이 존재합니다.");
        }
        return PointRefundResponse.of(refund);
    }

    public PointRefundResponse refundPointByChild(Long userId, ChildPointRefundRequest request) {
        User user = userRepository.findByIdWithLock(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        if (user.getRole() != UserRole.CHILD) {
            throw new UserException(ErrorCode.FORBIDDEN_CHILD_ONLY);
        }

        User depositTargetUser = userRepository.findById(request.getDepositTargetUserId())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        if (depositTargetUser.getBankName() == null || depositTargetUser.getBankAccount() == null) {
            throw new UserException(ErrorCode.EXCHANGE_ACCOUNT_NOT_REGISTERED);
        }

        if (user.getPoint() < request.getExchangeAmount()) {
            throw new PointException(ErrorCode.INSUFFICIENT_POINT_BALANCE);
        }

        user.exchangePointToMoney(request.getExchangeAmount());

        int processedAmount = request.getExchangeAmount();
        if (request.getExchangeAmount() < PointPolicy.EXCHANGE_FEE_EXEMPTION_AMOUNT) {
            processedAmount -= PointPolicy.CHILD_COMMISSION;
        }

        Refund refund = refundRepository.save(
                Refund.builder()
                        .requestedAmount(request.getExchangeAmount())
                        .processedAmount(processedAmount)
                        .remainingPoint(user.getPoint())
                        .status(RefundStatus.WAIT)
                        .user(user)
                        .depositTargetUser(depositTargetUser)
                        .build()
        );

        try {
            Notification notification = Notification.builder()
                    .receiver(user)
                    .message(processedAmount + "포인트를 꺼냈습니다!")
                    .subMessage("앱에 들어가서 확인해보세요!")
                    .type(NotificationType.POINT_REFUND)
                    .targetId(refund.getId())
                    .isRead(false)
                    .build();
            notificationRepository.save(notification);
        } catch (DataIntegrityViolationException e) {
            log.warn("이미 동일한 알림이 존재합니다.");
        }

        return PointRefundResponse.of(refund);
    }

    @Transactional(readOnly = true)
    public List<LatestRefundDepositTargetResponse> getRefundLatestDepositTarget(Long userId) {
        return refundRepository.findRefundDepositTargetByUserId(userId);
    }

    @Transactional(readOnly = true)
    public CustomPageResponse<RefundHistoryResponse> getRefundHistory(Long userId, Pageable pageable) {
        return CustomPageResponse.of(refundRepository.findRefundHistoryByUserId(userId, pageable));
    }

    public void cancelRefund(Long userId, Long refundId) {
        User user = userRepository.findByIdWithLock(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new PointException(ErrorCode.REFUND_NOT_FOUND));

        user.cancelRefund(refund.getRequestedAmount());

        refundRepository.deleteById(refund.getId());
    }
}
