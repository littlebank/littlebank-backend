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
import java.util.ArrayList;
import java.util.List;

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
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MissionSubject subject;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MissionType type;
    @Column(nullable = false)
    private int reward;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;
    @Column(nullable = false)
    private Boolean isDeleted;
    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MissionAssignment> assignments = new ArrayList<>();
    @Builder
    public Mission(String title, MissionSubject subject, MissionType type, int reward, LocalDateTime startDate, LocalDateTime endDate, User createdBy, Boolean isDeleted) {
        this.title = title;
        this.subject = subject;
        this.type = type;
        this.reward = reward;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdBy = createdBy;
        this.isDeleted = isDeleted == null ? false : isDeleted;
    }
}
