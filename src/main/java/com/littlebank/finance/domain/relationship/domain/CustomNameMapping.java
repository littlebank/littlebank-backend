package com.littlebank.finance.domain.relationship.domain;

import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Entity
@SQLDelete(sql = "UPDATE relationship_custom_name SET is_deleted = true WHERE relationship_custom_name_id = ?")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "custom_name_mapping",
        uniqueConstraints = @UniqueConstraint(columnNames = {"from_user_id", "to_user_id"}))
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
    public CustomNameMapping(Boolean isDeleted, User fromUser, User toUser, String customName) {
        super(isDeleted);
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.customName = customName;
    }

    public void updateCustomName(String customName) {
        this.customName = customName;
    }

}
