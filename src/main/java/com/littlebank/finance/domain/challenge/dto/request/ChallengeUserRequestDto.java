package com.littlebank.finance.domain.challenge.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeUserRequestDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private String subject;
    private LocalTime startTime;
    private Integer totalStudyTime;
    private Integer reward;
}
