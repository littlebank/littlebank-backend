package com.littlebank.finance.domain.goal.domain.repository.impl;

import com.littlebank.finance.domain.family.domain.QFamily;
import com.littlebank.finance.domain.family.domain.QFamilyMember;
import com.littlebank.finance.domain.goal.domain.Goal;
import com.littlebank.finance.domain.goal.domain.GoalCategory;
import com.littlebank.finance.domain.goal.domain.QGoal;
import com.littlebank.finance.domain.goal.domain.repository.CustomGoalRepository;
import com.littlebank.finance.domain.goal.dto.response.ChildGoalResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.littlebank.finance.domain.family.domain.QFamily.family;
import static com.littlebank.finance.domain.family.domain.QFamilyMember.familyMember;
import static com.littlebank.finance.domain.goal.domain.QGoal.goal;

@RequiredArgsConstructor
public class CustomGoalRepositoryImpl implements CustomGoalRepository {
    private final JPAQueryFactory queryFactory;
    private QGoal g = goal;
    private QFamily f = family;
    private QFamilyMember fm = familyMember;
    private final static int MONDAY = 1;

    public Boolean existsCategoryAndWeekly(Long userId, GoalCategory category) {
        Boolean exists = queryFactory
                .selectOne()
                .from(g)
                .where(g.createdBy.id.eq(userId)
                        .and(
                                g.category.eq(category)
                        )
                        .and(
                                Expressions.stringTemplate(
                                                "YEARWEEK({0}, {1})", g.createdDate, MONDAY
                                        )
                                        .eq(
                                                Expressions.stringTemplate("YEARWEEK(CURDATE(), {0})", MONDAY)
                                        )
                        )
                )
                .fetchOne() != null;

        return exists;
    }

    public List<Goal> findByCreatedByAndWeekly(Long userId) {
        List<Goal> results = queryFactory
                .selectFrom(g)
                .where(g.createdBy.id.eq(userId)
                        .and(
                                Expressions.stringTemplate(
                                        "YEARWEEK({0}, {1})", g.createdDate, MONDAY
                                        )
                                        .eq(
                                                Expressions.stringTemplate(
                                                        "YEARWEEK(CURDATE(), {0})", MONDAY
                                                )
                                        )
                        )
                )
                .fetch();

        return results;
    }

    public List<ChildGoalResponse> findChildWeeklyGoalResponses(Long familyId) {
        return queryFactory
                .select(Projections.constructor(
                        ChildGoalResponse.class,
                        g.id,
                        g.title,
                        g.category,
                        g.reward,
                        g.startDate,
                        g.endDate,
                        g.status,
                        fm.id,
                        fm.nickname
                ))
                .from(g)
                .join(g.family, f)
                .join(f.members, fm)
                .where(
                        g.family.id.eq(familyId)
                                .and(
                                        Expressions.stringTemplate("YEARWEEK({0}, {1})", g.createdDate, MONDAY)
                                                .eq(Expressions.stringTemplate("YEARWEEK(CURDATE(), {0})", MONDAY))
                                )
                                .and(g.createdBy.id.eq(fm.user.id))
                )
                .fetch();
    }

    public Page<ChildGoalResponse> findAllChildGoalResponses(Long familyId, Pageable pageable) {
        List<ChildGoalResponse> results = queryFactory
                .select(Projections.constructor(
                        ChildGoalResponse.class,
                        g.id,
                        g.title,
                        g.category,
                        g.reward,
                        g.startDate,
                        g.endDate,
                        g.status,
                        fm.id,
                        fm.nickname
                ))
                .from(g)
                .join(g.family, f)
                .join(f.members, fm)
                .where(
                        g.family.id.eq(familyId)
                                .and(g.createdBy.id.eq(fm.user.id))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, results.size());
    }

}
