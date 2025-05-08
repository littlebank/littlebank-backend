package com.littlebank.finance.domain.feed.domain.repository;

import com.littlebank.finance.domain.feed.domain.Feed;
import com.littlebank.finance.domain.feed.domain.FeedReport;
import com.littlebank.finance.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedReportRepository extends JpaRepository<FeedReport, Long> {
    boolean existsByFeedAndReporter(Feed feed, User reporter);
}
