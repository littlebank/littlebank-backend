package com.littlebank.finance.global.common;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncMailSendService {

    private final JavaMailSender javaMailSender;

    @Async
    public void send(MimeMessage emailForm) {
        javaMailSender.send(emailForm);
    }
}
