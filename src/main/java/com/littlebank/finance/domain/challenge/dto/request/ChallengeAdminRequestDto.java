package com.littlebank.finance.domain.challenge.dto.request;

import com.littlebank.finance.domain.challenge.domain.ChallengeCategory;
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
public class ChallengeAdminRequestDto {
    private String title;
    private String category;
    private String description;
    private String subject;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalStudyTime;
    private Integer totalParticipants;
}
