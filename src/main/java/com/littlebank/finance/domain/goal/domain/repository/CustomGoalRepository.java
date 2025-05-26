package com.littlebank.finance.domain.goal.domain.repository;

import com.littlebank.finance.domain.goal.domain.Goal;
import com.littlebank.finance.domain.goal.domain.GoalCategory;
import com.littlebank.finance.domain.goal.dto.response.ChildGoalResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomGoalRepository {
    Boolean existsCategoryAndWeekly(Long userId, GoalCategory category);

    List<Goal> findByCreatedByAndWeekly(Long userId);

    List<ChildGoalResponse> findChildWeeklyGoalResponses(Long familyId);
    Page<ChildGoalResponse> findAllChildGoalResponses(Long familyId, Pageable pageable);
}
