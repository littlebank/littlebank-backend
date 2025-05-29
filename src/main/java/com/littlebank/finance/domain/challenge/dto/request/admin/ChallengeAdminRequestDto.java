package com.littlebank.finance.domain.challenge.dto.request.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeAdminRequestDto {
    private String title;
    private String category;
    private String description;
    private String subject;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer totalStudyTime;
    private Integer totalParticipants;
}
