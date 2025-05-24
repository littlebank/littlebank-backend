package com.littlebank.finance.domain.goal.domain.repository;

import com.littlebank.finance.domain.goal.domain.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalRepository extends JpaRepository<Goal, Long> {
}
