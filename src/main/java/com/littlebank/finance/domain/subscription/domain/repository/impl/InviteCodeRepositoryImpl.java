package com.littlebank.finance.domain.subscription.domain.repository.impl;

import com.littlebank.finance.domain.subscription.domain.QInviteCode;
import com.littlebank.finance.domain.subscription.domain.repository.CustomInviteCodeRepository;
import com.littlebank.finance.domain.subscription.dto.response.InviteCodeResponseDto;
import com.littlebank.finance.domain.user.domain.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.littlebank.finance.domain.subscription.domain.QInviteCode.inviteCode;
import static com.littlebank.finance.domain.user.domain.QUser.user;

@RequiredArgsConstructor
public class InviteCodeRepositoryImpl implements CustomInviteCodeRepository {
    private final JPAQueryFactory queryFactory;
    private final QUser u = user;
    private final QInviteCode c = inviteCode;
    @Override
    public List<InviteCodeResponseDto> findAllByOwnerId(Long ownerId) {
        return queryFactory.select(Projections.constructor(
                        InviteCodeResponseDto.class,
                        c.code,
                        c.used,
                        c.redeemedBy.id,
                        c.redeemedBy.name
                ))
                .from(c)
                .leftJoin(c.redeemedBy)
                .where(c.subscription.owner.id.eq(ownerId))
                .fetch();
    }
}
