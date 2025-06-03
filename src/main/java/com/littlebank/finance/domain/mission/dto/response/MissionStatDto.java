package com.littlebank.finance.domain.mission.dto.response;

import com.littlebank.finance.domain.mission.domain.MissionCategory;
import com.littlebank.finance.domain.mission.domain.MissionStatus;
import com.littlebank.finance.domain.mission.domain.MissionSubject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MissionStatDto {
    private Long childId;
    private MissionCategory category;
    private MissionSubject subject;
    private MissionStatus status;
    private int count;

    public MissionStatDto(Long childId, MissionCategory category, MissionSubject subject, MissionStatus status, Long count) {
        this.childId = childId;
        this.category = category;
        this.subject = subject;
        this.status = status;
        this.count = count != null ? count.intValue() : 0;
    }
}
