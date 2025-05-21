package com.littlebank.finance.domain.challenge.dto.request;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public class ChallengeUserRequestDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private String subject;
    private LocalTime startTime;
    private Integer totalStudyTime;
    private Integer reward;
}
