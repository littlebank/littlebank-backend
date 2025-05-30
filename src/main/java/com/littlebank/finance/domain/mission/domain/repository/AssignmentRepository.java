package com.littlebank.finance.domain.mission.domain.repository;

import com.littlebank.finance.domain.mission.domain.MissionAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<MissionAssignment, Long> {
}
