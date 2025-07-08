package com.littlebank.finance.domain.ranking.domain.repository.impl;

import com.littlebank.finance.domain.challenge.domain.ChallengeStatus;
import com.littlebank.finance.domain.challenge.domain.QChallengeParticipation;
import com.littlebank.finance.domain.friend.domain.QFriend;
import com.littlebank.finance.domain.goal.domain.GoalStatus;
import com.littlebank.finance.domain.goal.domain.QGoal;
import com.littlebank.finance.domain.mission.domain.MissionStatus;
import com.littlebank.finance.domain.mission.domain.QMission;
import com.littlebank.finance.domain.ranking.domain.repository.CustomRankingRepository;
import com.littlebank.finance.domain.ranking.dto.response.GoalRankingResponseDto;
import com.littlebank.finance.domain.user.domain.QUser;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

import static com.littlebank.finance.domain.challenge.domain.QChallengeParticipation.challengeParticipation;
import static com.littlebank.finance.domain.friend.domain.QFriend.friend;
import static com.littlebank.finance.domain.goal.domain.QGoal.goal;
import static com.littlebank.finance.domain.mission.domain.QMission.mission;
import static com.littlebank.finance.domain.user.domain.QUser.user;

@RequiredArgsConstructor
public class CustomRankingRepositoryImpl implements CustomRankingRepository {
    private final JPAQueryFactory queryFactory;
    private QUser u = user;
    private QFriend f = friend;
    private QMission m = mission;
    private QGoal g = goal;
    private QChallengeParticipation cp = challengeParticipation;

    @Override
    public GoalRankingResponseDto findMonthlyTargetUserStat(Long userId, Long targetId, YearMonth month) {
        LocalDate startDate = month.atDay(1);
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = month.atEndOfMonth().atTime(23, 59, 59);

        Tuple userInfo = queryFactory
                .select(u.name, f.customName, u.targetAmount)
                .from(u)
                .leftJoin(f).on(
                        f.fromUser.id.eq(userId)
                                .and(f.toUser.id.eq(targetId))
                                .and(f.isBlocked.isFalse())
                )
                .where(u.id.eq(targetId))
                .fetchOne();
        String displayName = "";
        String name = userInfo != null ? userInfo.get(u.name) : "";
        String customName = userInfo != null ? userInfo.get(f.customName) : null;
        Integer targetAmount = userInfo != null ? userInfo.get(u.targetAmount) : 0;

        displayName = (customName != null && !customName.isBlank()) ? customName : name;

        Long missionTotal = queryFactory.select(m.count())
                .from(m)
                .where(m.child.id.eq(userId), m.startDate.between(start, end))
                .fetchOne();
        Long missionCompleted = queryFactory.select(m.count())
                .from(m)
                .where(m.child.id.eq(userId), m.status.eq(MissionStatus.ACHIEVEMENT), m.startDate.between(start, end))
                .fetchOne();
        Integer missionReward = queryFactory.select(m.reward.sum())
                .from(m)
                .where(m.child.id.eq(userId), m.status.eq(MissionStatus.ACHIEVEMENT), m.isRewarded.eq(true), m.startDate.between(start, end))
                .fetchOne();

        Long challengeTotal = queryFactory.select(cp.count())
                .from(cp)
                .where(cp.user.id.eq(userId), cp.startDate.between(start, end))
                .fetchOne();
        Long challengeCompleted = queryFactory.select(cp.count())
                .from(cp)
                .where(cp.user.id.eq(userId), cp.challengeStatus.eq(ChallengeStatus.ACHIEVEMENT), cp.startDate.between(start, end))
                .fetchOne();
        Integer challengeReward = queryFactory.select(cp.reward.sum())
                .from(cp)
                .where(cp.user.id.eq(userId), cp.challengeStatus.eq(ChallengeStatus.ACHIEVEMENT), cp.isRewarded.eq(true), cp.startDate.between(start, end))
                .fetchOne();

        Long goalTotal = queryFactory.select(g.count())
                .from(g)
                .where(g.createdBy.id.eq(userId), g.startDate.between(start, end))
                .fetchOne();
        Long goalCompleted = queryFactory.select(g.count())
                .from(g)
                .where(g.createdBy.id.eq(userId), g.status.eq(GoalStatus.ACHIEVEMENT), g.startDate.between(start, end))
                .fetchOne();
        Integer goalReward = queryFactory.select(g.reward.sum())
                .from(g)
                .where(g.createdBy.id.eq(userId), g.status.eq(GoalStatus.ACHIEVEMENT), g.isRewarded.eq(true), g.startDate.between(start, end))
                .fetchOne();
        return GoalRankingResponseDto.builder()
                .userId(targetId)
                .name(displayName)
                .missionTotal(missionTotal != null ? missionTotal : 0L)
                .missionCompleted(missionCompleted != null ? missionCompleted : 0L)
                .missionReward(missionReward != null ? missionReward : 0)
                .challengeTotal(challengeTotal != null ? challengeTotal : 0L)
                .challengeCompleted(challengeCompleted != null ? challengeCompleted : 0L)
                .challengeReward(challengeReward != null ? challengeReward : 0)
                .goalTotal(goalTotal != null ? goalTotal : 0L)
                .goalCompleted(goalCompleted != null ? goalCompleted : 0L)
                .goalReward(goalReward != null ? goalReward : 0)
                .targetAmount(targetAmount != null ? targetAmount : 0)
                .build();
    }
}
