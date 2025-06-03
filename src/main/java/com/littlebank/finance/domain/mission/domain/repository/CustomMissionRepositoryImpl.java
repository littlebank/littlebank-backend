package com.littlebank.finance.domain.mission.domain.repository;

import com.littlebank.finance.domain.friend.domain.QFriend;
import com.littlebank.finance.domain.mission.domain.QMission;
import com.littlebank.finance.domain.mission.dto.response.MissionStatDto;
import com.littlebank.finance.domain.user.domain.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.time.LocalDateTime;
@RequiredArgsConstructor
public class CustomMissionRepositoryImpl implements CustomMissionRepository{
    private QFriend f = QFriend.friend;
    private QMission m = QMission.mission;
    private QUser u = QUser.user;
    private final JPAQueryFactory queryFactory;
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
