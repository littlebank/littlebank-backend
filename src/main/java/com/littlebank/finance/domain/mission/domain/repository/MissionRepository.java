package com.littlebank.finance.domain.mission.domain.repository;

import com.littlebank.finance.domain.mission.domain.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, Long> {
}
