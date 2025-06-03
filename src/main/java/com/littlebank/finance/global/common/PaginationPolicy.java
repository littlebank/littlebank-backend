package com.littlebank.finance.global.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginationPolicy {
    // General
    public final static int GENERAL_PAGE_SIZE = 20;

    // Challenge
    public static final int CHALLENGE_LIST_PAGE_SIZE = 10;
    public static final int MISSION_LIST_PAGE_SIZE = 10;
}
