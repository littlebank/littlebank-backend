package com.littlebank.finance.domain.user.service;

import com.littlebank.finance.domain.user.dto.request.EmailSendRequest;
import com.littlebank.finance.domain.user.dto.response.EmailSendResponse;
import com.littlebank.finance.domain.user.exception.MailException;
import com.littlebank.finance.global.common.AsyncMailSendService;
import com.littlebank.finance.global.error.exception.ErrorCode;
import com.littlebank.finance.global.mail.MailPolicy;
import com.littlebank.finance.global.util.FileUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class MailService {
    private final AsyncMailSendService asyncMailSendService;
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.verification.sender}")
    private String senderEmail;

    public EmailSendResponse sendSimpleEmail(EmailSendRequest request) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            createEmailForm(helper, request.getToEmail());

            asyncMailSendService.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new MailException(ErrorCode.MAIL_SEND_ERROR);
        }
        return EmailSendResponse.of(request.getToEmail());
    }

    private void createEmailForm(MimeMessageHelper helper, String toEmail) throws MessagingException, UnsupportedEncodingException {
        helper.setSubject(MailPolicy.EMAIL_CHECK_SEND_TITLE);
        helper.setTo(toEmail);
        helper.setFrom(senderEmail, MailPolicy.DEFAULT_SENDER_NAME);
        helper.setText(FileUtil.readFileFromResource(MailPolicy.EMAIL_CHECK_SEND_FORM_PATH), true);
    }
}
