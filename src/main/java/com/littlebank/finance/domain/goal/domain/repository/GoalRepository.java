package com.littlebank.finance.domain.goal.domain.repository;

import com.littlebank.finance.domain.goal.domain.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface GoalRepository extends JpaRepository<Goal, Long>, CustomGoalRepository{
    List<Goal> findByCreatedById(Long userId);
    default Map<Long, String> findIdTitleMapByIds(List<Long> ids) {
        return findAllById(ids).stream()
                .collect(Collectors.toMap(Goal::getId, Goal::getTitle));
    }
}
