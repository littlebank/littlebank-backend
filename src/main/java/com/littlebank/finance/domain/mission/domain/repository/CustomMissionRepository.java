package com.littlebank.finance.domain.mission.domain.repository;


import com.littlebank.finance.domain.mission.dto.response.MissionStatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomMissionRepository {
    List<MissionStatDto> getMissionStatsByPeriod(List<Long> friendIds, LocalDateTime start, LocalDateTime end);
}
