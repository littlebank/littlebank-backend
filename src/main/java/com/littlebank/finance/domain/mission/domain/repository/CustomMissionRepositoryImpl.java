package com.littlebank.finance.domain.mission.domain.repository;

import com.littlebank.finance.domain.mission.domain.*;
import com.littlebank.finance.domain.mission.dto.response.MissionRecentRewardResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.littlebank.finance.domain.mission.domain.QMission.mission;

@RequiredArgsConstructor
public class CustomMissionRepositoryImpl implements CustomMissionRepository {
    private final JPAQueryFactory queryFactory;
    private QMission m = mission;

    @Override
    public Integer findRecentReward(Long childId, MissionType type, MissionCategory category, MissionSubject subject) {
        Mission result = queryFactory
                .selectFrom(m)
                .where(m.child.id.eq(childId),
                        m.type.eq(type),
                        m.status.eq(MissionStatus.ACHIEVEMENT),
                        m.category.eq(category),
                        category == MissionCategory.LEARNING ? m.subject.eq(subject) : null)
                .orderBy(m.endDate.desc())
                .fetchFirst();
        return result != null ? result.getReward() : 0;
    }
}
