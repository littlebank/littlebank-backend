package com.littlebank.finance.domain.goal.domain.repository.impl;

import com.littlebank.finance.domain.goal.domain.GoalCategory;
import com.littlebank.finance.domain.goal.domain.QGoal;
import com.littlebank.finance.domain.goal.domain.repository.CustomGoalRepository;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.littlebank.finance.domain.goal.domain.QGoal.goal;

@RequiredArgsConstructor
public class CustomGoalRepositoryImpl implements CustomGoalRepository {

    private final JPAQueryFactory queryFactory;
    private QGoal g = goal;

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
                                        "YEARWEEK({0}, {1})", g.createdDate, 1
                                ).eq(
                                        Expressions.stringTemplate("YEARWEEK(CURDATE(), {0})", 1)
                                )
                        )
                )
                .fetchOne() != null;

        return exists;
    }


}
