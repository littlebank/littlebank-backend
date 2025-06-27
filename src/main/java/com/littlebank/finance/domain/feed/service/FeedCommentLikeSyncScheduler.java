package com.littlebank.finance.domain.feed.service;

import com.littlebank.finance.domain.feed.domain.repository.FeedCommentRepository;
import com.littlebank.finance.global.error.exception.ErrorCode;
import com.littlebank.finance.global.redis.RedisDao;
import com.littlebank.finance.global.redis.RedisPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedCommentLikeSyncScheduler {
    private final RedisDao redisDao;
    private final FeedCommentRepository feedCommentRepository;

    @Transactional
    @Scheduled(fixedRate = 300000)
    public void syncLikeCountsToDatabase() {
        Set<String> commentIds = redisDao.getSetMembers("changedCommentIds");
        if (commentIds == null || commentIds.isEmpty()) return;

        for (String commentId : commentIds) {
            try {
                String likeSetKey = RedisPolicy.FEED_COMMENT_LIKE_SET_KEY_PREFIX + commentId;
                int likeCount = redisDao.getSetSize(likeSetKey);
                feedCommentRepository.updateLikeCount(Long.valueOf(commentId), likeCount);
            } catch (Exception e) {
                log.warn("좋아요 동기화 실패: comment={}", commentId, e);
            }
        }
        redisDao.deleteSet("changedCommentIds");
    }
}
