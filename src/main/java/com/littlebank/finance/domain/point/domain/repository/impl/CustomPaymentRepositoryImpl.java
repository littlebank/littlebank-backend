package com.littlebank.finance.domain.point.domain.repository.impl;

import com.littlebank.finance.domain.point.domain.PaymentStatus;
import com.littlebank.finance.domain.point.domain.QPayment;
import com.littlebank.finance.domain.point.domain.repository.CustomPaymentRepository;
import com.littlebank.finance.domain.point.dto.response.PaymentHistoryResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.littlebank.finance.domain.point.domain.QPayment.payment;

@RequiredArgsConstructor
public class CustomPaymentRepositoryImpl implements CustomPaymentRepository {
    private final JPAQueryFactory queryFactory;
    private QPayment p = payment;

    @Override
    public Page<PaymentHistoryResponse> findHistoryByUserId(Long userId, Pageable pageable) {
        List<PaymentHistoryResponse> results =
                queryFactory.select(Projections.constructor(
                                PaymentHistoryResponse.class,
                                p.id,
                                p.amount,
                                p.remainingPoint,
                                p.pgProvider,
                                p.payMethod,
                                p.paidAt
                        ))
                        .from(p)
                        .where(p.user.id.eq(userId)
                                .and(p.status.eq(PaymentStatus.PAID)))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(p.paidAt.desc())
                        .fetch();

        return new PageImpl<>(results, pageable, results.size());
    }
}
