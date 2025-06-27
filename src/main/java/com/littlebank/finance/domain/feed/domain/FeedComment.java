package com.littlebank.finance.domain.feed.domain;

import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="feed_comment")
@SQLDelete(sql = "UPDATE feed_comment SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedComment extends BaseEntity {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 1000, nullable = false)
    private String content;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private FeedComment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedComment> replies = new ArrayList<>();

    @OneToMany(mappedBy = "feedComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedCommentLike> likes = new ArrayList<>();

    @Column(name = "like_count", nullable = false)
    private int likeCount = 0;


    @Builder
    public FeedComment(Feed feed, User user, String content, FeedComment parent) {
        this.feed = feed;
        this.user = user;
        this.content = content;
        this.parent = parent;
    }

    public void update(@NotBlank @Size(max = 500) String content) {
        this.content = content;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public List<FeedCommentLike> getLikes() {
        return likes;
    }


    public void setLikeCount(int redisLikecount) {
        this.likeCount = redisLikecount;
    }
}
