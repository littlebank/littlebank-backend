package com.littlebank.finance.global.mail;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MailPolicy {
    public final static String DEFAULT_SENDER_NAME = "Little Bank";
    public final static String EMAIL_CHECK_SEND_TITLE = "[LittleBank] 리틀뱅크 이메일 확인 안내 메일입니다.";
    public final static String EMAIL_CHECK_SEND_FORM_PATH = "templates/check-email.html";
}
