package com.littlebank.finance.domain.mission.domain.repository;

import com.littlebank.finance.domain.friend.domain.QFriend;
import com.littlebank.finance.domain.mission.domain.QMission;
import com.littlebank.finance.domain.mission.dto.response.MissionStatDto;
import com.littlebank.finance.domain.user.domain.QUser;
import com.littlebank.finance.domain.mission.domain.*;
import com.littlebank.finance.domain.mission.dto.response.MissionRecentRewardResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import com.querydsl.core.types.Projections;
import java.util.List;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class CustomMissionRepositoryImpl implements CustomMissionRepository{
    private QFriend f = QFriend.friend;
    private QMission m = QMission.mission;
    private QUser u = QUser.user;
    private final JPAQueryFactory queryFactory;


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

    @Override
    public List<MissionStatDto> getMissionStatsByPeriod(List<Long> friendIds, LocalDateTime start, LocalDateTime end) {
        return queryFactory
                .select(Projections.constructor(MissionStatDto.class,
                        m.child.id,
                        m.category,
                        m.subject,
                        m.status,
                        m.count()
                ))
                .from(m)
                .where(
                        m.child.id.in(friendIds),
                        m.endDate.between(start, end)
                )
                .groupBy(m.child.id, m.category, m.subject, m.status)
                .fetch();
    }
}
