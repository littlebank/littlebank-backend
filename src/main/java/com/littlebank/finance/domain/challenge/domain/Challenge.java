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
import java.time.LocalDateTime;

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
    @Column(nullable = false, length = 100)
    private String title;
    @Enumerated(EnumType.STRING)
    private ChallengeCategory category;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;
    @Column(name = "subject", length = 20, nullable = false)
    private String subject;
    @Column(name = "total_participants")
    private Integer totalParticipants;
    @Column(name = "current_participants")
    private Integer currentParticipants;
    @Column(nullable = false)
    private Boolean isDeleted = false;

    private int viewCount = 0;

    @Builder
    public Challenge(String title, ChallengeCategory category, LocalDateTime startDate, LocalDateTime endDate, String subject, Integer totalParticipants, Integer currentParticipants,  int viewCount) {
        this.title = title;
        this.category = category;
        this.subject = subject;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalParticipants = totalParticipants;
        this.currentParticipants = currentParticipants;
        this.viewCount = viewCount;
    }

    public void update(String title, ChallengeCategory category, String subject, LocalDateTime startDate, LocalDateTime endDate, Integer totalParticipants) {
        this.title = title;
        this.category = category;
        this.subject = subject;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalParticipants = totalParticipants;
    }

    public void setCurrentParticipants(int i) {
        this.currentParticipants += 1;
    }

    public void increaseViewCount() {
        this.viewCount += 1;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
