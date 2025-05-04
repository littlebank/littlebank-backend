package com.littlebank.finance.domain.friend.domain;

import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@SQLDelete(sql = "UPDATE friend SET is_deleted = true WHERE friend_id = ?")
@Where(clause = "is_deleted = false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user", nullable = false)
    private User fromUser;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user", nullable = false)
    private User toUser;
    @Column(length = 50, nullable = false)
    private String customName;
    @Column(nullable = false)
    private Boolean isBlocked;

    @Builder
    public Friend(Boolean isDeleted, User fromUser, User toUser, String customName, Boolean isBlocked) {
        super(isDeleted);
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.customName = customName;
        this.isBlocked = isBlocked == null ? false : isBlocked;
    }
}
