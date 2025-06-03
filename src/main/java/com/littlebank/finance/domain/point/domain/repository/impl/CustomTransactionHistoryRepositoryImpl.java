package com.littlebank.finance.domain.point.domain.repository.impl;

import com.littlebank.finance.domain.point.domain.QTransactionHistory;
import com.littlebank.finance.domain.point.domain.repository.CustomTransactionHistoryRepository;
import com.littlebank.finance.domain.point.dto.response.ReceivePointHistoryResponse;
import com.littlebank.finance.domain.user.domain.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


import java.util.List;

import static com.littlebank.finance.domain.point.domain.QTransactionHistory.transactionHistory;
import static com.littlebank.finance.domain.user.domain.QUser.user;

@RequiredArgsConstructor
public class CustomTransactionHistoryRepositoryImpl implements CustomTransactionHistoryRepository {
    private final JPAQueryFactory queryFactory;
    private QTransactionHistory th = transactionHistory;
    private QUser u = user;

    @Override
    public Page<ReceivePointHistoryResponse> findReceivedPointHistoryByUserId(Long userId, Pageable pageable) {
        List<ReceivePointHistoryResponse> results = queryFactory
                .select(Projections.constructor(
                        ReceivePointHistoryResponse.class,
                        th.id,
                        th.pointAmount,
                        th.message,
                        th.receiverRemainingPoint,
                        u.id,
                        u.name,
                        th.createdDate
                ))
                .from(th)
                .join(th.sender, u)
                .where(th.receiver.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(th.createdDate.desc())
                .fetch();

        return new PageImpl<>(results, pageable, results.size());
    }
}
