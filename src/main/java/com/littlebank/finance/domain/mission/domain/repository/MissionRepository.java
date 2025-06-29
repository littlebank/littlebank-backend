package com.littlebank.finance.domain.mission.domain.repository;

import com.littlebank.finance.domain.mission.domain.Mission;
import com.littlebank.finance.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public interface MissionRepository extends JpaRepository<Mission, Long>, CustomMissionRepository {
    Page<Mission> findByChild(User user, Pageable pageable);
    default Map<Long, String> findIdTitleMapByIds(List<Long> ids) {
        return findAllById(ids).stream()
                .collect(Collectors.toMap(Mission::getId, Mission::getTitle));
    }
}
