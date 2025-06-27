package com.littlebank.finance.domain.feed.domain.repository;

import com.littlebank.finance.domain.feed.domain.FeedComment;
import com.littlebank.finance.domain.feed.domain.FeedCommentLike;
import com.littlebank.finance.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedCommentLikeRepository extends JpaRepository<FeedCommentLike, Long> {
    Optional<FeedCommentLike> findByFeedCommentAndUser(FeedComment feedComment, User user);
}
