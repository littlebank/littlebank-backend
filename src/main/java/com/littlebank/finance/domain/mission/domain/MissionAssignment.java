package com.littlebank.finance.domain.mission.domain;

import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionAssignment extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_assignment_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private User child;
    @Enumerated(EnumType.STRING)
    private MissionStatus status = MissionStatus.PENDING;
    private float progress = 0.0f;
    private LocalDateTime acceptedAt;
    private LocalDateTime completedAt;
    @Column(nullable = false)
    private Boolean isDeleted;

    @Builder
    public MissionAssignment(Mission mission, User child, MissionStatus status, LocalDateTime acceptedAt, LocalDateTime completedAt, Boolean isDeleted) {
        this.mission = mission;
        this.child = child;
        this.status = status;
        this.acceptedAt = acceptedAt;
        this.completedAt = completedAt;
        this.isDeleted = isDeleted == null ? false : isDeleted;
    }

    public void accept() {
        this.status = MissionStatus.IN_PROGRESS;
        this.acceptedAt = LocalDateTime.now();
    }

    public void complete() {
        this.status = MissionStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
        this.progress = 1.0f;
    }
}
