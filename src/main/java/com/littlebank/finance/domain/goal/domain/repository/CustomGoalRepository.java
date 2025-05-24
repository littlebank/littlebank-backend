package com.littlebank.finance.domain.goal.domain.repository;

import com.littlebank.finance.domain.goal.domain.GoalCategory;

public interface CustomGoalRepository {
    Boolean existsCategoryAndWeekly(Long userId, GoalCategory category);
}
