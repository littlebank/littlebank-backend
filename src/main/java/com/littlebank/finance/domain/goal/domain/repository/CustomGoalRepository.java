package com.littlebank.finance.domain.goal.domain.repository;

import com.littlebank.finance.domain.goal.domain.Goal;
import com.littlebank.finance.domain.goal.domain.GoalCategory;

import java.util.List;

public interface CustomGoalRepository {
    Boolean existsCategoryAndWeekly(Long userId, GoalCategory category);
    List<Goal> findByCreatedByAndWeekly(Long userId);
}
