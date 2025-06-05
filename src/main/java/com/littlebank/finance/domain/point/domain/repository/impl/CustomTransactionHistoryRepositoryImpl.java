package com.littlebank.finance.domain.point.domain.repository.impl;

import com.littlebank.finance.domain.point.domain.QTransactionHistory;
import com.littlebank.finance.domain.point.domain.repository.CustomTransactionHistoryRepository;
import com.littlebank.finance.domain.point.dto.response.LatestSentAccountResponse;
import com.littlebank.finance.domain.point.dto.response.ReceivePointHistoryResponse;
import com.littlebank.finance.domain.point.dto.response.SendPointHistoryResponse;
import com.littlebank.finance.domain.user.domain.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


import java.util.List;

import static com.littlebank.finance.domain.point.domain.QTransactionHistory.transactionHistory;

@RequiredArgsConstructor
public class CustomTransactionHistoryRepositoryImpl implements CustomTransactionHistoryRepository {
    private final JPAQueryFactory queryFactory;
    private QTransactionHistory th = transactionHistory;
    private QUser sender = new QUser("sender");
    private QUser receiver = new QUser("receiver");

    @Override
    public Page<LatestSentAccountResponse> findLatestSentAccountByUserId(Long userId, Pageable pageable) {
        List<LatestSentAccountResponse> results = queryFactory
                .select(Projections.constructor(
                        LatestSentAccountResponse.class,
                        receiver.id,
                        receiver.name,
                        receiver.bankName,
                        receiver.bankCode,
                        receiver.bankAccount,
                        th.createdDate
                ))
                .from(th)
                .join(th.receiver, receiver)
                .where(th.sender.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(th.createdDate.desc())
                .fetch();

        return new PageImpl<>(results, pageable, results.size());
    }

    @Override
    public List<ReceivePointHistoryResponse> findReceivedPointHistoryByUserId(Long userId) {
        List<ReceivePointHistoryResponse> results = queryFactory
                .select(Projections.constructor(
                        ReceivePointHistoryResponse.class,
                        th.id,
                        Expressions.constant("RECEIVE"),
                        th.pointAmount,
                        Expressions.stringTemplate(
                                "COALESCE(NULLIF({0}, ''), {1})",
                                th.message,
                                Expressions.stringTemplate("CONCAT({0}, '의 ', {1}, '포인트 선물')", sender.name, th.pointAmount)
                        ),
                        th.receiverRemainingPoint,
                        sender.id,
                        sender.name,
                        th.createdDate
                ))
                .from(th)
                .join(th.sender, sender)
                .where(th.receiver.id.eq(userId))
                .fetch();

        return results;
    }

    @Override
    public List<SendPointHistoryResponse> findSentPointHistoryByUserId(Long userId) {
        List<SendPointHistoryResponse> results = queryFactory
                .select(Projections.constructor(
                        SendPointHistoryResponse.class,
                        th.id,
                        Expressions.constant("SEND"),
                        th.pointAmount,
                        Expressions.stringTemplate(
                                "COALESCE(NULLIF({0}, ''), {1})",
                                th.message,
                                Expressions.stringTemplate("CONCAT({0}, '에게 ', {1}, '포인트 선물')", receiver.name, th.pointAmount)
                        ),
                        th.senderRemainingPoint,
                        receiver.id,
                        receiver.name,
                        th.createdDate
                ))
                .from(th)
                .join(th.receiver, receiver)
                .where(th.sender.id.eq(userId))
                .fetch();

        return results;
    }
}
