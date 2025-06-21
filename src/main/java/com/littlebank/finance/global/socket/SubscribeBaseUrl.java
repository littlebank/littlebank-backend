package com.littlebank.finance.global.socket;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubscribeBaseUrl {
    // user
    public final static String USER_SUBSCRIBE_BASE_URL = "/sub/user/";

    // chat
    public final static String CHAT_SUBSCRIBE_BASE_URL = "/sub/chat/";
}
