package com.littlebank.finance.domain.family.domain.repository.impl;

import com.littlebank.finance.domain.family.domain.Family;
import com.littlebank.finance.domain.family.domain.QFamily;
import com.littlebank.finance.domain.family.domain.QFamilyMember;
import com.littlebank.finance.domain.family.domain.Status;
import com.littlebank.finance.domain.family.domain.repository.CustomFamilyRepository;
import com.littlebank.finance.domain.user.domain.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.littlebank.finance.domain.family.domain.QFamily.family;
import static com.littlebank.finance.domain.family.domain.QFamilyMember.familyMember;
import static com.littlebank.finance.domain.user.domain.QUser.user;

@RequiredArgsConstructor
public class CustomFamilyRepositoryImpl implements CustomFamilyRepository {
    private final JPAQueryFactory queryFactory;
    private QUser u = user;
    private QFamilyMember m = familyMember;
    private QFamily f = family;

    @Override
    public Optional<Family> findByUserIdWithMember(Long userId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(f)
                        .join(m).on(m.family.id.eq(f.id)).fetchJoin()
                        .join(u).on(u.id.eq(m.user.id)).fetchJoin()
                        .where(m.user.id.eq(userId)
                                .and(m.status.eq(Status.JOINED)))
                        .fetchOne());
    }
}
