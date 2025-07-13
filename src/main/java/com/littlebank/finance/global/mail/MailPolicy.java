package com.littlebank.finance.global.mail;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MailPolicy {
    public final static String DEFAULT_SENDER_NAME = "Little Bank";

    // 이메일 확인 관련
    public final static String EMAIL_CHECK_SEND_TITLE = "[리틀뱅크] 이메일 확인 안내 메일입니다.";
    public final static String EMAIL_CHECK_SEND_FORM_PATH = "templates/check-email.html";

    // 임시 비밀번호 재발급 관련
    public final static String PASSWORD_REISSUE_SEND_TITLE = "[리틀뱅크] 임시 비밀번호 발급 메일입니다.";
    public final static String PASSWORD_REISSUE_SEND_FORM_PATH = "reissue-password";

    // 신고 관련
    public static final String REPORT_MAIL_TITLE = "[LittleBank] 신고 접수 안내";
    public static final String REPORT_MAIL_TEMPLATE_PATH = "templates/report-email.html";
}