package com.littlebank.finance.domain.feed.domain.repository;

import com.littlebank.finance.domain.feed.domain.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    Page<Feed> findByUserId(Long userId, Pageable pageable);
    @EntityGraph(attributePaths = "comments")
    Optional<Feed> findWithCommentsById(Long feedId);

    @Modifying
    @Query("UPDATE Feed f SET f.likeCount = :likeCount WHERE f.id = :feedId")
    void updateLikeCount(@Param("feedId") Long feedId, @Param("likeCount") Integer likeCount);
}
