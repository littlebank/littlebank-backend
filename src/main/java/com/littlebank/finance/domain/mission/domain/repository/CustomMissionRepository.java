package com.littlebank.finance.domain.mission.domain.repository;


import com.littlebank.finance.domain.mission.domain.Mission;
import com.littlebank.finance.domain.mission.dto.response.MissionStatDto;
import com.littlebank.finance.domain.mission.domain.MissionCategory;
import com.littlebank.finance.domain.mission.domain.MissionSubject;
import com.littlebank.finance.domain.mission.domain.MissionType;
import com.littlebank.finance.domain.mission.dto.response.MissionRecentRewardResponseDto;
import com.littlebank.finance.domain.notification.dto.response.MissionAchievementNotificationDto;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomMissionRepository {
    Integer findRecentReward(Long childId, MissionType type, MissionCategory category, MissionSubject subject);
    List<MissionStatDto> getMissionStatsByPeriod(List<Long> friendIds, LocalDateTime start, LocalDateTime end);
    List<Mission> updateExpiredMissionsToAchievement();
    List<MissionAchievementNotificationDto> findMissionAchievementNotificationDto();
}
