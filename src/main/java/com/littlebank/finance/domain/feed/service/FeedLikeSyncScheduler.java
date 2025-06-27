package com.littlebank.finance.domain.feed.service;

import com.littlebank.finance.domain.feed.domain.repository.FeedRepository;
import com.littlebank.finance.global.redis.RedisDao;
import com.littlebank.finance.global.redis.RedisPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Service

@RequiredArgsConstructor
public class FeedLikeSyncScheduler {
    private final FeedRepository feedRepository;
    private final RedisDao redisDao;

    @Transactional
    @Scheduled(fixedRate = 300000)
    public void syncLikeCountsToDatabase() {
        Set<String> feedIds = redisDao.getSetMembers("changedFeedIds");
        if (feedIds == null || feedIds.isEmpty()) return;

        for (String feedId : feedIds) {
            try {
                String likeSetKey = RedisPolicy.FEED_LIKE_SET_KEY_PREFIX + feedId;
                int likeCount = redisDao.getSetSize(likeSetKey);
                feedRepository.updateLikeCount(Long.valueOf(feedId), likeCount);
            } catch (Exception e) {
                log.warn("좋아요 동기화 실패: feed={}", feedId, e);
            }
        }
        redisDao.deleteSet("changedFeedIds");
    }
}
