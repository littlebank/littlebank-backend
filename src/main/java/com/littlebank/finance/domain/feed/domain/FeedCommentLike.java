package com.littlebank.finance.domain.feed.domain;

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
@Table(
        name="feed_comment_like",
        uniqueConstraints = @UniqueConstraint(columnNames = {"comment_id", "user_id"})
)@Getter

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedCommentLike extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private FeedComment feedComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Builder
    public FeedCommentLike(FeedComment feedComment, User user) {
        this.feedComment = feedComment;
        this.user = user;
    }
}
