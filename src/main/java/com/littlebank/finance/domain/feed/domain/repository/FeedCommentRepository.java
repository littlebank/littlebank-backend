package com.littlebank.finance.domain.feed.domain.repository;

import com.littlebank.finance.domain.feed.domain.Feed;
import com.littlebank.finance.domain.feed.domain.FeedComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {
    Page<FeedComment> findByFeedAndParentIsNullAndIsDeletedFalse(Feed feed, Pageable pageable);
    Page<FeedComment> findByParentAndIsDeletedFalse(FeedComment parent, Pageable pageable);

    @Modifying
    @Query("UPDATE FeedComment fc SET fc.likeCount = :likeCount WHERE fc.id = :commentId")
    void updateLikeCount(@Param("commentId") Long commentId, @Param("likeCount") Integer likeCount);
}
