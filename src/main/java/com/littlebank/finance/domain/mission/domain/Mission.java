package com.littlebank.finance.domain.mission.domain;

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
@SQLDelete(sql = "UPDATE mission SET is_deleted = true WHERE mission_id = ?")
@Where(clause = "is_deleted = false")
@Table(name="mission")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mission extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_id")
    private Long id;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MissionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "mission_category", nullable = false)
    private MissionCategory category;

    @Enumerated(EnumType.STRING)
    private MissionSubject subject;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MissionStatus status;

    @Column(nullable = false)
    private Integer reward;

    @Column(name = "is_rewarded", nullable = false)
    private Boolean isRewarded;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private User child;

    @Column(name = "finish_score")
    private Integer finishScore;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Builder
    public Mission(String title, MissionType type, MissionCategory category, MissionSubject subject, MissionStatus status, Integer reward, Boolean isRewarded, LocalDateTime startDate, LocalDateTime endDate, User createdBy, User child, Boolean isDeleted, Integer finishScore) {
        this.title = title;
        this.type = type;
        this.category = category;
        this.subject = subject;
        this.status = status;
        this.reward = reward;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdBy = createdBy;
        this.child = child;
        this.finishScore = finishScore;
        this.isRewarded = isRewarded;
        this.isDeleted = isDeleted == null ? false : isDeleted;
    }

    public void acceptProposal() {
        this.status = MissionStatus.ACCEPT;
    }

    public void storeScore(Integer score) {
        this.finishScore = score;
    }

    public void setStatus(MissionStatus missionStatus) {
        this.status = MissionStatus.ACHIEVEMENT;
    }

    public void rewarded(Boolean isRewarded) {
        this.isRewarded = isRewarded;
    }
}
