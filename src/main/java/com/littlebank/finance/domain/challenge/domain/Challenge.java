package com.littlebank.finance.domain.challenge.domain;

import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "challenge")
@SQLDelete(sql = "UPDATE challenge SET is_deleted = true WHERE challenge_id = ?")
@Where(clause = "is_deleted = false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Challenge extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_id")
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Enumerated(EnumType.STRING)
    private ChallengeCategory category;
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    @Column(name = "total_study_time", nullable = false)
    private Integer totalStudyTime;
    @Column(name = "max_participants", nullable = false)
    private Integer maxParticipants;
    @Column(name = "preferred_start_time")
    private LocalTime preferredStartTime;
    @Column(name = "preferred_reward")
    private Integer preferredReward;
    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;
    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Builder
    public Challenge(String title, String description, ChallengeCategory category, LocalDate startDate, LocalDate endDate, Integer totalStudyTime, Integer maxParticipants, LocalTime preferredStartTime, Integer preferredReward, Boolean isDeleted) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalStudyTime = totalStudyTime;
        this.maxParticipants = maxParticipants;
        this.preferredStartTime = preferredStartTime;
        this.preferredReward = preferredReward;
    }
}
