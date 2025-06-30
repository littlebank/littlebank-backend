package com.littlebank.finance.domain.point.domain.repository.impl;

import com.littlebank.finance.domain.point.domain.constant.PaymentStatus;
import com.littlebank.finance.domain.point.domain.QPayment;
import com.littlebank.finance.domain.point.domain.constant.RewardType;
import com.littlebank.finance.domain.point.domain.repository.CustomPaymentRepository;
import com.littlebank.finance.domain.point.dto.response.PaymentHistoryResponse;
import com.littlebank.finance.domain.point.dto.response.ReceivePointHistoryResponse;
import com.littlebank.finance.domain.user.domain.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.littlebank.finance.domain.point.domain.QPayment.payment;
import static com.littlebank.finance.domain.user.domain.QUser.user;

@RequiredArgsConstructor
public class CustomPaymentRepositoryImpl implements CustomPaymentRepository {
    private final JPAQueryFactory queryFactory;
    private QPayment p = payment;
    private QUser u = user;

    @Override
    public Page<PaymentHistoryResponse> findHistoryByUserId(Long userId, Pageable pageable) {
        List<PaymentHistoryResponse> results =
                queryFactory.select(Projections.constructor(
                                PaymentHistoryResponse.class,
                                p.id,
                                p.amount,
                                p.remainingPoint,
                                u.name,
                                p.pgProvider,
                                p.payMethod,
                                p.paidAt
                        ))
                        .from(p)
                        .join(u).on(u.id.eq(p.user.id))
                        .where(p.user.id.eq(userId)
                                .and(p.status.eq(PaymentStatus.PAID)))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(p.paidAt.desc())
                        .fetch();

        return new PageImpl<>(results, pageable, results.size());
    }

    @Override
    public List<ReceivePointHistoryResponse> findPaymentHistoryByUserId(Long userId) {
        List<ReceivePointHistoryResponse> results = queryFactory
                .select(Projections.constructor(
                        ReceivePointHistoryResponse.class,
                        p.id,
                        Expressions.constant("PAYMENT"),
                        p.amount,
                        Expressions.stringTemplate("CONCAT({0}, '에서 ', {1}, '포인트 충전')", p.pgProvider, p.amount),
                        p.remainingPoint,
                        Expressions.nullExpression(Long.class),
                        p.pgProvider,
                        p.paidAt,
                        Expressions.nullExpression(RewardType.class),
                        Expressions.nullExpression(Long.class)
                ))
                .from(p)
                .where(p.user.id.eq(userId)
                        .and(p.status.eq(PaymentStatus.PAID)))
                .fetch();

        return results;
    }
}
