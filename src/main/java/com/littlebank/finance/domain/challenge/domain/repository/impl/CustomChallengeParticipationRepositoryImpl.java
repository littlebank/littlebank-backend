package com.littlebank.finance.domain.challenge.domain.repository.impl;

import com.littlebank.finance.domain.challenge.domain.ChallengeParticipation;
import com.littlebank.finance.domain.challenge.domain.ChallengeStatus;
import com.littlebank.finance.domain.challenge.domain.QChallenge;
import com.littlebank.finance.domain.challenge.domain.QChallengeParticipation;
import com.littlebank.finance.domain.challenge.domain.repository.CustomChallengeParticipationRepository;
import com.littlebank.finance.domain.family.domain.QFamilyMember;
import com.littlebank.finance.domain.family.domain.Status;
import com.littlebank.finance.domain.notification.dto.response.ChallengeAchievementNotificationDto;
import com.littlebank.finance.domain.user.domain.QUser;
import com.littlebank.finance.domain.user.domain.UserRole;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class CustomChallengeParticipationRepositoryImpl implements CustomChallengeParticipationRepository {

    private final JPAQueryFactory queryFactory;

    QChallengeParticipation cp = QChallengeParticipation.challengeParticipation;
    QChallenge c = QChallenge.challenge;
    QUser u = QUser.user;
    QFamilyMember fm = QFamilyMember.familyMember;

    @Override
    public Page<ChallengeParticipation> findOngoingParticipations(Long userId, Pageable pageable) {
        List<ChallengeParticipation> content = queryFactory
                .selectFrom(cp)
                .join(cp.challenge, c).fetchJoin()
                .join(cp.user, u).fetchJoin()
                .where(
                        cp.user.id.eq(userId),
                        cp.isDeleted.isFalse(),
                        cp.challenge.isDeleted.isFalse(),
                        cp.challengeStatus.eq(ChallengeStatus.ACCEPT),
                        cp.challenge.endDate.goe(LocalDateTime.now())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable,
                () -> queryFactory
                        .select(cp.count())
                        .from(cp)
                        .where(
                                cp.user.id.eq(userId),
                                cp.isDeleted.isFalse(),
                                cp.challenge.isDeleted.isFalse(),
                                cp.challengeStatus.eq(ChallengeStatus.ACCEPT),
                                cp.challenge.endDate.goe(LocalDateTime.now())
                        )
                        .fetchOne());
    }

    @Override
    public Page<ChallengeParticipation> findCompletedParticipations(Long userId, Pageable pageable) {
        List<ChallengeParticipation> content = queryFactory
                .selectFrom(cp)
                .join(cp.challenge, c).fetchJoin()
                .join(cp.user, u).fetchJoin()
                .where(
                        cp.user.id.eq(userId),
                        cp.isDeleted.isFalse(),
                        cp.challenge.isDeleted.isFalse(),
                        cp.challengeStatus.eq(ChallengeStatus.ACCEPT),
                        cp.challenge.endDate.lt(LocalDateTime.now())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable,
                () -> queryFactory
                        .select(cp.count())
                        .from(cp)
                        .where(
                                cp.user.id.eq(userId),
                                cp.isDeleted.isFalse(),
                                cp.challenge.isDeleted.isFalse(),
                                cp.challengeStatus.eq(ChallengeStatus.ACCEPT),
                                cp.challenge.endDate.lt(LocalDateTime.now())
                        )
                        .fetchOne());
    }

    @Override
    public List<ChallengeParticipation> updateExpiredChallengesToAchievement() {
        List<ChallengeParticipation> expired = queryFactory.selectFrom(cp)
                .where(
                        cp.endDate.before(LocalDateTime.now()),
                        cp.challengeStatus.eq(ChallengeStatus.ACCEPT),
                        cp.isDeleted.isFalse()
                ).fetch();
        expired.forEach(p -> p.setChallengeStatus(ChallengeStatus.ACHIEVEMENT));
        return expired;
    }

    @Override
    public List<ChallengeAchievementNotificationDto> findChallengeAchievementNotificationDto() {
        QFamilyMember parentMember = new QFamilyMember("parentMember");
        LocalDateTime yesterdayStart = LocalDate.now().minusDays(1).atStartOfDay();
        LocalDateTime yesterdayEnd = LocalDate.now().minusDays(1).atTime(23, 59, 59);
        List<ChallengeAchievementNotificationDto> results = queryFactory.select(
                Projections.constructor(ChallengeAchievementNotificationDto.class,
                        parentMember.user.id,
                        fm.nickname,
                        cp.title,
                        cp.id
                        ))
                .from(cp)
                .join(fm).on(fm.user.id.eq(cp.user.id))
                .join(parentMember).on(parentMember.family.id.eq(fm.family.id))
                .join(u).on(u.id.eq(parentMember.user.id)) // 부모 유저
                .where(
                        cp.challengeStatus.eq(ChallengeStatus.ACHIEVEMENT),
                        cp.endDate.between(yesterdayStart, yesterdayEnd),
                        cp.isDeleted.isFalse(),
                        fm.status.eq(Status.JOINED),
                        parentMember.status.eq(Status.JOINED),
                        u.role.eq(UserRole.PARENT)
                )
                .fetch();
        return results;
    }

}