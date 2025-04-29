package com.littlebank.finance.domain.feed.domain;

import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="feed_comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedComment extends BaseEntity {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Feed feed;

    @ManyToOne
    private User user;

    private String content;

    @Builder
    public FeedComment(Feed feed, User user, String content) {
        this.feed = feed;
        this.user = user;
        this.content = content;
    }
}
