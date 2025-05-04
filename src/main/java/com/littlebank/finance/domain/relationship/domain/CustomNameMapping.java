package com.littlebank.finance.domain.relationship.domain;

import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "custom_name_mapping",
        uniqueConstraints = @UniqueConstraint(columnNames = {"from_user", "to_user"}))
public class CustomNameMapping extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "custom_name_mapping_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user", nullable = false)
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user", nullable = false)
    private User toUser;

    @Column(length = 50, nullable = false)
    private String customName;

    @Builder
    public CustomNameMapping(User fromUser, User toUser, String customName) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.customName = customName;
    }

    public void updateCustomName(String customName) {
        this.customName = customName;
    }

}
