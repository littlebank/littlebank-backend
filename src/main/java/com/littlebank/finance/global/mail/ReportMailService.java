package com.littlebank.finance.global.mail;

import com.littlebank.finance.global.util.FileUtil;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportMailService {

    private final JavaMailSender emailSender;

    @Value("${report.mail.to}")
    private String reportReceiver;

    public void sendReportEmail(String type, Long targetId, Long reporterId, Long targetUserId, String targetUserName, String targetContent, String reporterName) {
        try {
            String htmlTemplate = FileUtil.readFileFromResource(MailPolicy.REPORT_MAIL_TEMPLATE_PATH);
            String htmlBody = htmlTemplate
                    .replace("${type}", type)
                    .replace("${targetId}", String.valueOf(targetId))
                    .replace("${reporterId}", String.valueOf(reporterId))
                    .replace("${targetUserId}", String.valueOf(targetUserId))
                    .replace("${targetUserName}", String.valueOf(targetUserName))
                    .replace("${targetContent}", String.valueOf(targetContent))
                    .replace("${reporterName}", reporterName);

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(reportReceiver);
            helper.setSubject(MailPolicy.REPORT_MAIL_TITLE);
            helper.setText(htmlBody, true);

            emailSender.send(message);

        } catch (Exception e) {
            // 로깅 또는 사용자 친화적인 예외 처리
            throw new RuntimeException("신고 메일 전송 실패", e);
        }
    }
}