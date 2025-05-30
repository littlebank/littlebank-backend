package com.littlebank.finance.global.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.littlebank.finance.domain.notification.domain.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FirebaseService {
    private final static String NOTIFICATION_IMAGE_URL = "https://avatars.githubusercontent.com/u/203592524?s=96&v=4"; // 임시

    @Async
    public void sendNotification(Notification notification) {
        Message message = Message.builder()
                .setToken(notification.getReceiver().getFcmToken())
                .setNotification(
                        com.google.firebase.messaging.Notification.builder()
                                .setTitle(notification.getMessage())
                                .setBody(notification.getSubMessage())
                                .setImage(NOTIFICATION_IMAGE_URL)
                                .build()
                )
                .build();
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("FCM 전송 성공! 메시지 ID: " + response);
        } catch (FirebaseMessagingException e) {
            log.error("FCM 전송 실패: " + e.getMessage());
        }
    }
}
