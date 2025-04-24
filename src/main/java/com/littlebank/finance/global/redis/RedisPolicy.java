package com.littlebank.finance.global.redis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisPolicy {

    public static final String BLACKLIST_KEY = "BLACKLIST:";
}
