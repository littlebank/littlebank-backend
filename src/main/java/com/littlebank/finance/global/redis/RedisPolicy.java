package com.littlebank.finance.global.redis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisPolicy {
    public static final String LOGIN_USER_KEY_PREFIX = "LOGIN:";
    public static final String CERTIFICATION_CODE_KEY_PREFIX = "CERTIFICATION_CODE:";
    public static final String FEED_LIKE_SET_KEY_PREFIX = "FEED:LIKE:";
    public static final String FEED_COMMENT_LIKE_SET_KEY_PREFIX = "COMMENT:LIKE:";
    public static final String FEED_LIKE_LOCK_KEY_PREFIX = "FEED:LIKE:LOCK:";
    public static final String FEED_COMMENT_LIKE_LOCK_KEY_PREFIX = "COMMENT:LIKE:LOCK:";
    public static final String CHALLENGE_JOIN_KEY_PREFIX = "CHALLENGE:JOIN:";
    public static final String CHALLENGE_CURRENT_COUNT_KEY_PREFIX = "CHALLENGE:CURRENT_COUNT:";
    public static final String GAME_VOTE_LOCK_PREFIX = "GAME:VOTE:";
    public static final String SURVEY_VOTE_PREFIX = "QUIZ:VOTE:";
    public static final String SURVEY_VOTE_LOCK_PREFIX = "QUIZ:VOTE:LOCK:";

    public static final int CERTIFICATION_CODE_TTL = 300;
}
