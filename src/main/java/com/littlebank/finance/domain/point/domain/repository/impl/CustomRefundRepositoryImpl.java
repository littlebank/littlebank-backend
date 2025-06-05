package com.littlebank.finance.domain.point.domain.repository.impl;

import com.littlebank.finance.domain.point.domain.QRefund;
import com.littlebank.finance.domain.point.domain.RefundStatus;
import com.littlebank.finance.domain.point.domain.repository.CustomRefundRepository;
import com.littlebank.finance.domain.point.dto.response.SendPointHistoryResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.littlebank.finance.domain.point.domain.QRefund.refund;

@RequiredArgsConstructor
public class CustomRefundRepositoryImpl implements CustomRefundRepository {
    private final JPAQueryFactory queryFactory;
    private QRefund r = refund;

    @Override
    public List<SendPointHistoryResponse> findRefundHistoryByUserId(Long userId) {
        List<SendPointHistoryResponse> results = queryFactory
                .select(Projections.constructor(
                        SendPointHistoryResponse.class,
                        r.id,
                        Expressions.constant("REFUND"),
                        r.requestedAmount,
                        Expressions.stringTemplate("CONCAT('내 계좌로 ', {0}, '원 환전')", r.processedAmount),
                        r.remainingPoint,
                        Expressions.nullExpression(Long.class),
                        Expressions.constant("내 계좌"),
                        r.exchangedAt
                ))
                .from(r)
                .where(r.user.id.eq(userId)
                        .and(r.status.eq(RefundStatus.PROCESSED)))
                .fetch();

        return results;
    }
}
