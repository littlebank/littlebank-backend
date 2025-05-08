package com.littlebank.finance.domain.feed.domain;

import com.littlebank.finance.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import com.littlebank.finance.domain.user.domain.User;

@Entity
@Table(name = "feed_report")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User reporter;

    @Column(length = 1000, nullable = false)
    private String reason;

    @Builder
    public FeedReport(Feed feed, User reporter, String reason) {
        this.feed = feed;
        this.reporter = reporter;
        this.reason = reason;
    }
}
