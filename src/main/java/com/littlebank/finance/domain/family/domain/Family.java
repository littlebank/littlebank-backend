package com.littlebank.finance.domain.family.domain;

import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SQLDelete(sql = "UPDATE family SET is_deleted = true WHERE family_id = ?")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Family extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "family_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;
    @Column(nullable = false)
    private Integer minStampCount; // 목표 달성 최소 도장 갯수
    @Column(nullable = false)
    private Boolean isDeleted;
    @OneToMany(mappedBy = "family", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FamilyMember> members = new ArrayList<>();

    @Builder
    public Family(User createdBy, Integer minStampCount, Boolean isDeleted) {
        this.createdBy = createdBy;
        this.minStampCount = minStampCount == null ? 7 : minStampCount;
        this.isDeleted = isDeleted == null ? false : isDeleted;
    }
}
