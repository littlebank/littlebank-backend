package com.littlebank.finance.domain.mission.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnalyzeReportResponse {
    private Long memberId;
    private String name;
    private Integer period;

    private Integer totalStudyTime; // 총 학습 시간
    private Integer lastMonthTotalStudyTime; // 지난 달 총 학습 시간
    private Integer thisMonthTotalStudyTime; // 이번 달 총 학습 시간

    private Integer prevStudyTime; // 이전 학습 시간
    private Integer recentStudyTime; // 최근 학습 시간

    private Integer prevMissionAchieveCount; // 이전 미션 달성 갯수
    private Integer recentMissionAchieveCount; // 최근 미션 달성 갯수

    private Integer prevAvgScore; // 이전 평균 점수
    private Integer recentAvgScore; // 최근 평균 점수

    private String nameOfLeastTimeSubject; // 적은 시간을 소요한 과목의 이름
    private Integer timeOfLeastTimeSubject; // 적은 시간을 소요한 과목의 소요 시간
    private String nameOfMostTimeSubject; // 많은 시간을 소요한 과목의 이름
    private Integer timeOfMostTimeSubject; // 많은 시간을 소요한 과목의 소요 시간
}
