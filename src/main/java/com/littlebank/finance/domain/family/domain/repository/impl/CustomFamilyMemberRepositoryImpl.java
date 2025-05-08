package com.littlebank.finance.domain.family.domain.repository.impl;

import com.littlebank.finance.domain.family.domain.FamilyMember;
import com.littlebank.finance.domain.family.domain.QFamily;
import com.littlebank.finance.domain.family.domain.QFamilyMember;
import com.littlebank.finance.domain.family.domain.repository.CustomFamilyMemberRepository;
import com.littlebank.finance.domain.user.domain.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.littlebank.finance.domain.family.domain.QFamily.family;
import static com.littlebank.finance.domain.family.domain.QFamilyMember.familyMember;
import static com.littlebank.finance.domain.user.domain.QUser.user;

@RequiredArgsConstructor
public class CustomFamilyMemberRepositoryImpl implements CustomFamilyMemberRepository {
    private final JPAQueryFactory queryFactory;
    private QUser u = user;
    private QFamilyMember m = familyMember;
    private QFamily f = family;

    @Override
    public Optional<FamilyMember> findByUserIdWithFamily(Long userId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(m)
                .join(m.family, f).fetchJoin()
                .where(m.user.id.eq(userId))
                .fetchOne());
    }
}
