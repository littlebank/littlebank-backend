package com.littlebank.finance.domain.feed.domain;

import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="feed_like")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 이게 맞나
public class FeedLike extends BaseEntity {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Feed feed;

    @ManyToOne
    private User user;

    @Builder
    public FeedLike(Feed feed, User user) {
        this.feed = feed;
        this.user = user;
    }
}
