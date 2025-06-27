package com.littlebank.finance.domain.goal.domain.repository;

import com.littlebank.finance.domain.goal.domain.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long>, CustomGoalRepository{
    List<Goal> findByCreatedById(Long userId);
}
