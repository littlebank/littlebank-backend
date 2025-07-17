package com.littlebank.finance.domain.mission.domain.repository.impl;

import com.littlebank.finance.domain.challenge.domain.ChallengeStatus;
import com.littlebank.finance.domain.challenge.domain.QChallengeParticipation;
import com.littlebank.finance.domain.family.domain.QFamilyMember;
import com.littlebank.finance.domain.goal.domain.Goal;
import com.littlebank.finance.domain.goal.domain.GoalCategory;
import com.littlebank.finance.domain.goal.domain.GoalStatus;
import com.littlebank.finance.domain.goal.domain.QGoal;
import com.littlebank.finance.domain.mission.domain.MissionCategory;
import com.littlebank.finance.domain.mission.domain.MissionStatus;
import com.littlebank.finance.domain.mission.domain.MissionSubject;
import com.littlebank.finance.domain.mission.domain.QMission;
import com.littlebank.finance.domain.mission.domain.repository.CustomAnalyzeRepository;
import com.littlebank.finance.domain.mission.dto.response.AnalyzeReportResponse;
import com.littlebank.finance.domain.user.domain.QUser;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static com.littlebank.finance.domain.challenge.domain.QChallengeParticipation.challengeParticipation;
import static com.littlebank.finance.domain.family.domain.QFamilyMember.familyMember;
import static com.littlebank.finance.domain.goal.domain.QGoal.goal;
import static com.littlebank.finance.domain.mission.domain.QMission.mission;
import static com.littlebank.finance.domain.user.domain.QUser.user;

@RequiredArgsConstructor
public class CustomAnalyzeRepositoryImpl implements CustomAnalyzeRepository {
    private final JPAQueryFactory queryFactory;
    private QMission m = mission;
    private QChallengeParticipation cp = challengeParticipation;
    private QGoal g = goal;
    private QUser u = user;
    private QFamilyMember fm = familyMember;

    @Override
    public Optional<AnalyzeReportResponse> getAnalyzeReportResponse(Long memberId, Integer period) {
        Tuple least = getSubjectTimeSummary(memberId, period, false);
        Tuple most = getSubjectTimeSummary(memberId, period, true);

        String nameOfLeast = least != null ? mapSubjectToKorean(least.get(m.subject)) : null;
        int timeOfLeast = least != null ? least.get(1, Integer.class) : 0;

        String nameOfMost = most != null ? mapSubjectToKorean(most.get(m.subject)) : null;
        int timeOfMost = most != null ? most.get(1, Integer.class) : 0;

        AnalyzeReportResponse response = new AnalyzeReportResponse(
                memberId,
                getMemberName(memberId),
                period,

                getTotalStudyTime(memberId), // 총 학습 시간
                getLastMonthStudyTime(memberId), // 지난 달 총 학습 시간
                getThisMonthStudyTime(memberId), // 이번 달 총 학습 시간

                getPreviousStudyTime(memberId, period), // 이전 공부 시간
                getRecentStudyTime(memberId, period), // 최근 공부 시간

                getPreviousMissionAchieveCount(memberId, period), // 이전 미션 달성 갯수
                getRecentMissionAchieveCount(memberId, period), // 최근 미션 달성 갯수

                getPreviousAverageScore(memberId, period), // 이전 평균 점수
                getRecentAverageScore(memberId, period), // 최근 평균 점수

                nameOfLeast,
                timeOfLeast,
                nameOfMost,
                timeOfMost
        );

        return Optional.of(response);
    }

    /**
     * 누적 총 학습 시간
     * @param memberId
     * @return
     */
    private int getTotalStudyTime(Long memberId) {
        LocalDateTime now = LocalDateTime.now().minusDays(1); // 오늘 제외

        // 1. 미션 학습 일 수 합
        Integer missionDays = queryFactory
                .select(Expressions.numberTemplate(Integer.class,
                        "SUM(CASE WHEN {0} <= {1} THEN DATEDIFF(LEAST({1}, {2}), {0}) + 1 ELSE 0 END)",
                        m.startDate,
                        Expressions.constant(now),
                        m.endDate))
                .from(m)
                .join(m.child, u)
                .join(fm).on(fm.user.id.eq(u.id))
                .where(fm.id.eq(memberId)
                        .and(m.status.in(MissionStatus.ACHIEVEMENT, MissionStatus.ACCEPT)))
                .fetchOne();

        // 2. 챌린지 학습 일 수 합
        Integer challengeDays = queryFactory
                .select(Expressions.numberTemplate(Integer.class,
                        "SUM(CASE WHEN {0} <= {1} THEN DATEDIFF(LEAST({1}, {2}), {0}) + 1 ELSE 0 END)",
                        cp.startDate,
                        Expressions.constant(now),
                        cp.endDate))
                .from(cp)
                .join(cp.user, u)
                .join(fm).on(fm.user.id.eq(u.id))
                .where(fm.id.eq(memberId)
                        .and(cp.challengeStatus.in(ChallengeStatus.ACHIEVEMENT, ChallengeStatus.ACCEPT)))
                .fetchOne();

        // 3. 목표 요일 합
        Integer goalDays = queryFactory
                .select(Expressions.numberTemplate(Integer.class,
                        "SUM({0} + {1} + {2} + {3} + {4} + {5} + {6})",
                        g.mon.castToNum(Integer.class),
                        g.tue.castToNum(Integer.class),
                        g.wed.castToNum(Integer.class),
                        g.thu.castToNum(Integer.class),
                        g.fri.castToNum(Integer.class),
                        g.sat.castToNum(Integer.class),
                        g.sun.castToNum(Integer.class)))
                .from(g)
                .join(g.createdBy, u)
                .join(fm).on(fm.user.id.eq(u.id))
                .where(fm.id.eq(memberId)
                        .and(g.status.in(GoalStatus.ACHIEVEMENT, GoalStatus.ACCEPT))
                        .and(g.category.eq(GoalCategory.LEARNING))
                        .and(g.startDate.loe(now)))
                .fetchOne();

        int mission = missionDays == null ? 0 : missionDays;
        int challenge = challengeDays == null ? 0 : challengeDays;
        int goal = goalDays == null ? 0 : goalDays;

        return (mission + challenge + goal) * 2;
    }

    /**
     * 지난 달 총 학습 시간
     */
    private int getLastMonthStudyTime(Long memberId) {
        YearMonth lastMonth = YearMonth.now().minusMonths(1);
        LocalDateTime start = lastMonth.atDay(1).atStartOfDay();
        LocalDateTime end = lastMonth.atEndOfMonth().atTime(LocalTime.MAX);
        return getStudyTimeInRange(memberId, start, end);
    }

    /**
     * 이번 달 총 학습 시간
     * @param memberId
     * @return
     */
    private int getThisMonthStudyTime(Long memberId) {
        YearMonth thisMonth = YearMonth.now();
        LocalDateTime start = thisMonth.atDay(1).atStartOfDay();
        LocalDateTime end = LocalDate.now().minusDays(1).atTime(LocalTime.MAX);
        return getStudyTimeInRange(memberId, start, end);
    }

    /**
     * 이전 공부 시간
     */
    private int getPreviousStudyTime(Long memberId, int period) {
        LocalDateTime end = LocalDateTime.now().minusDays(period + 1);       // period + 1일 전
        LocalDateTime start = end.minusDays(period - 1);                     // period * 2 - 1일 전
        return getStudyTimeInRange(memberId, start, end);
    }

    /**
     * 최근 공부 시간
     */
    private int getRecentStudyTime(Long memberId, int period) {
        LocalDateTime end = LocalDateTime.now().minusDays(1); // 하루 전
        LocalDateTime start = end.minusDays(period - 1);      // period일 전
        return getStudyTimeInRange(memberId, start, end);
    }

    /**
     * 범위 내 학습 기간 집계
     * @param memberId
     * @param start
     * @param end
     * @return
     */
    private int getStudyTimeInRange(Long memberId, LocalDateTime start, LocalDateTime end) {
        // === 1. 미션 & 챌린지 학습일 계산 (QueryDSL) ===
        NumberExpression<Integer> missionDays = Expressions.numberTemplate(
                Integer.class,
                "SUM(CASE WHEN {0} <= {1} THEN DATEDIFF(LEAST({1}, {2}), {0}) ELSE 0 END)",
                m.startDate,
                Expressions.constant(end),
                m.endDate
        );

        NumberExpression<Integer> challengeDays = Expressions.numberTemplate(
                Integer.class,
                "SUM(CASE WHEN {0} <= {1} THEN DATEDIFF(LEAST({1}, {2}), {0}) ELSE 0 END)",
                cp.startDate,
                Expressions.constant(end),
                cp.endDate
        );

        Tuple result = queryFactory
                .select(missionDays, challengeDays)
                .from(fm)
                .join(u).on(u.id.eq(fm.user.id))
                .leftJoin(m).on(m.child.id.eq(u.id).and(m.status.in(MissionStatus.ACCEPT, MissionStatus.ACHIEVEMENT)))
                .leftJoin(cp).on(cp.user.id.eq(u.id).and(cp.challengeStatus.in(ChallengeStatus.ACCEPT, ChallengeStatus.ACHIEVEMENT)))
                .where(
                        fm.id.eq(memberId),
                        m.startDate.loe(end),
                        m.endDate.goe(start)
                )
                .fetchOne();

        int missionTime = result.get(missionDays) != null ? result.get(missionDays) : 0;
        int challengeTime = result.get(challengeDays) != null ? result.get(challengeDays) : 0;

        // === 2. 목표 학습일 계산 (Java) ===
        List<Goal> goals = queryFactory
                .selectFrom(g)
                .join(fm).on(g.createdBy.id.eq(fm.user.id))
                .where(
                        fm.id.eq(memberId),
                        g.status.in(GoalStatus.ACCEPT, GoalStatus.ACHIEVEMENT),
                        g.category.eq(GoalCategory.LEARNING),
                        g.startDate.loe(end),
                        g.endDate.goe(start)
                )
                .fetch();

        int goalDays = 0;

        for (Goal goal : goals) {
            LocalDate gStart = goal.getStartDate().toLocalDate();
            LocalDate gEnd = goal.getEndDate().toLocalDate();

            for (LocalDate date = gStart; !date.isAfter(gEnd); date = date.plusDays(1)) {
                if (date.isBefore(start.toLocalDate()) || date.isAfter(end.toLocalDate())) continue;

                DayOfWeek day = date.getDayOfWeek();
                switch (day) {
                    case MONDAY: if (goal.getMon()) goalDays++; break;
                    case TUESDAY: if (goal.getTue()) goalDays++; break;
                    case WEDNESDAY: if (goal.getWed()) goalDays++; break;
                    case THURSDAY: if (goal.getThu()) goalDays++; break;
                    case FRIDAY: if (goal.getFri()) goalDays++; break;
                    case SATURDAY: if (goal.getSat()) goalDays++; break;
                    case SUNDAY: if (goal.getSun()) goalDays++; break;
                }
            }
        }

        // === 3. 최종 학습 시간 = (미션 + 챌린지 + 목표) * 2
        int totalDays = missionTime + challengeTime + goalDays;
        return totalDays * 2;
    }

    /**
     * 최근 미션 달성 수
     */
    private int getRecentMissionAchieveCount(Long memberId, int period) {
        LocalDateTime end = LocalDateTime.now().minusDays(1);
        LocalDateTime start = LocalDateTime.now().minusDays(period);
        return getMissionAchieveCount(memberId, start, end);
    }

    /**
     * 이전 미션 달성 수
     */
    private int getPreviousMissionAchieveCount(Long memberId, int period) {
        LocalDateTime end = LocalDateTime.now().minusDays(period + 1);
        LocalDateTime start = LocalDateTime.now().minusDays(period * 2);
        return getMissionAchieveCount(memberId, start, end);
    }

    /**
     * 특정 기간 내 미션 달성 수
     */
    private int getMissionAchieveCount(Long memberId, LocalDateTime start, LocalDateTime end) {
        Long count = queryFactory
                .select(m.count())
                .from(fm)
                .join(u).on(u.id.eq(fm.user.id))
                .join(m).on(m.child.id.eq(u.id))
                .where(
                        fm.id.eq(memberId),
                        m.status.eq(MissionStatus.ACHIEVEMENT),
                        m.endDate.between(start, end)
                )
                .fetchOne();

        return count == null ? 0 : count.intValue();
    }

    /**
     * 이전 평균 점수
     */
    private int getPreviousAverageScore(Long memberId, int period) {
        LocalDateTime end = LocalDateTime.now().minusDays(period + 1);
        LocalDateTime start = LocalDateTime.now().minusDays(period * 2);
        return getAverageScore(memberId, start, end);
    }

    /**
     * 최근 평균 점수
     */
    private int getRecentAverageScore(Long memberId, int period) {
        LocalDateTime end = LocalDateTime.now().minusDays(1);
        LocalDateTime start = LocalDateTime.now().minusDays(period);
        return getAverageScore(memberId, start, end);
    }

    /**
     * 특정 기간 내 평균 점수 (미션 + 챌린지)
     */
    private int getAverageScore(Long memberId, LocalDateTime start, LocalDateTime end) {
        Double avgMission = queryFactory
                .select(m.finishScore.avg())
                .from(fm)
                .join(u).on(u.id.eq(fm.user.id))
                .join(m).on(m.child.id.eq(u.id))
                .where(
                        fm.id.eq(memberId),
                        m.status.eq(MissionStatus.ACHIEVEMENT),
                        m.endDate.between(start, end)
                )
                .fetchOne();

        Double avgChallenge = queryFactory
                .select(cp.finishScore.avg())
                .from(fm)
                .join(u).on(u.id.eq(fm.user.id))
                .join(cp).on(cp.user.id.eq(u.id))
                .where(
                        fm.id.eq(memberId),
                        cp.challengeStatus.eq(ChallengeStatus.ACHIEVEMENT),
                        cp.endDate.between(start, end)
                )
                .fetchOne();

        return (int) Math.round(
                Stream.of(avgMission, avgChallenge)
                        .filter(Objects::nonNull)
                        .mapToDouble(Double::doubleValue)
                        .average()
                        .orElse(0.0)
        );
    }

    private Tuple getSubjectTimeSummary(Long memberId, int period, boolean isMost) {
        LocalDateTime rangeStart = LocalDateTime.now().minusDays(period);
        LocalDateTime rangeEnd = LocalDateTime.now().minusDays(1); // 오늘 제외

        // 과목별 학습 시간 (교집합 날짜 일수 * 2)
        NumberExpression<Integer> overlapDays = Expressions.numberTemplate(
                Integer.class,
                "CASE WHEN {0} <= {1} AND {2} >= {3} THEN DATEDIFF(LEAST({1}, {2}), GREATEST({0}, {3})) + 1 ELSE 0 END",
                m.startDate, // {0}
                Expressions.constant(rangeEnd), // {1}
                m.endDate,   // {2}
                Expressions.constant(rangeStart) // {3}
        );

        NumberExpression<Integer> calculatedDuration = overlapDays.multiply(2);

        List<Tuple> result = queryFactory
                .select(m.subject, calculatedDuration.sum())
                .from(m)
                .join(u).on(m.child.id.eq(u.id))
                .join(fm).on(fm.user.id.eq(u.id))
                .where(
                        fm.id.eq(memberId),
                        m.status.in(MissionStatus.ACCEPT, MissionStatus.ACHIEVEMENT),
                        m.category.eq(MissionCategory.LEARNING),
                        m.startDate.loe(rangeEnd),
                        m.endDate.goe(rangeStart)
                )
                .groupBy(m.subject)
                .orderBy(isMost
                        ? calculatedDuration.sum().desc()
                        : calculatedDuration.sum().asc())
                .fetch();

        return result.isEmpty() ? null : result.get(0);
    }

    private String mapSubjectToKorean(MissionSubject subject) {
        return switch (subject) {
            case KOREAN -> "국어";
            case ENGLISH -> "영어";
            case MATH -> "수학";
            case SOCIAL -> "사회";
            case SCIENCE -> "과학";
        };
    }

    private String getMemberName(Long memberId) {
        return queryFactory
                .select(fm.nickname)
                .from(fm)
                .where(fm.id.eq(memberId))
                .fetchFirst();
    }
}


