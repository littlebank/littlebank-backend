package com.littlebank.finance.domain.mission.domain.repository;

import com.littlebank.finance.domain.mission.domain.Mission;
import com.littlebank.finance.domain.mission.domain.MissionStatus;
import com.littlebank.finance.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    Optional<Mission> findTopByChildIdAndStatusOrderByEndDateDesc(Long UserId, MissionStatus missionStatus);

    Page<Mission> findByChild(User user, Pageable pageable);
}
