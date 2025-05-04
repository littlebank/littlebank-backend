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
public class Relationship extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relationship_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user", nullable = false)
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user", nullable = false)
    private User toUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RelationshipType relationshipType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RelationshipStatus relationshipStatus;

    @Builder
    public Relationship(
            User fromUser, User toUser, RelationshipType relationshipType,
            RelationshipStatus relationshipStatus
    ) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.relationshipType = relationshipType;
        this.relationshipStatus = relationshipStatus;
    }

    public void updateStatusByConnection() {
        this.relationshipStatus = RelationshipStatus.CONNECTED;
    }
}
