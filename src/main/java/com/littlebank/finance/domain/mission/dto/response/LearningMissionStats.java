package com.littlebank.finance.domain.mission.dto.response;

import com.littlebank.finance.domain.mission.domain.MissionSubject;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LearningMissionStats {
    private MissionSubject subject;
    private int totalSubjectMissionCount;
    private int completedSubjectMissionCount;
    private double subjectCompletionRate;

    public static LearningMissionStats of(MissionSubject subject, int totalSubjectMissionCount, int completedSubjectMissionCount, double subjectCompletionRate) {
        return LearningMissionStats.builder()
                .subject(subject)
                .totalSubjectMissionCount(totalSubjectMissionCount)
                .completedSubjectMissionCount(completedSubjectMissionCount)
                .subjectCompletionRate(subjectCompletionRate)
                .build();
    }
}
