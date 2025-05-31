package com.littlebank.finance.domain.mission.domain.repository;

import com.littlebank.finance.domain.mission.domain.MissionCategory;
import com.littlebank.finance.domain.mission.domain.MissionSubject;
import com.littlebank.finance.domain.mission.domain.MissionType;
import com.littlebank.finance.domain.mission.dto.response.MissionRecentRewardResponseDto;

public interface CustomMissionRepository {
    Integer findRecentReward(Long childId, MissionType type, MissionCategory category, MissionSubject subject);
}
