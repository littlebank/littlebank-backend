package com.littlebank.finance.domain.goal.domain;

import com.littlebank.finance.domain.family.domain.Family;
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
@SQLDelete(sql = "UPDATE goal SET is_deleted = true WHERE goal_id = ?")
@Where(clause = "is_deleted = false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Goal extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Long id;
    @Column(length = 50, nullable = false)
    private String title;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoalCategory category;
    @Column(nullable = false)
    private Integer reward;
    @Column(nullable = false)
    private LocalDateTime startDate;
    @Column(nullable = false)
    private LocalDateTime endDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoalStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family", nullable = false)
    private Family family;
    @Column(nullable = false)
    private Boolean isDeleted;

    @Builder
    public Goal(String title, GoalCategory category, Integer reward, LocalDateTime startDate, LocalDateTime endDate,
                GoalStatus status, User createdBy, Family family, Boolean isDeleted) {
        this.title = title;
        this.category = category;
        this.reward = reward;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.createdBy = createdBy;
        this.family = family;
        this.isDeleted = isDeleted == null ? false : isDeleted;
    }
}
