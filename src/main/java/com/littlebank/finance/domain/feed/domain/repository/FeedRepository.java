package com.littlebank.finance.domain.feed.domain.repository;

import com.littlebank.finance.domain.feed.domain.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    List<Feed> findAllByOrderByCreatedDateDesc();
    //List<Feed> findAllByOrderByLikeCountDesc(Long userId);
}
