package com.littlebank.finance.domain.challenge.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeUserRequestDto {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime startTime;
    private Integer totalStudyTime;
    private Integer reward;
}
