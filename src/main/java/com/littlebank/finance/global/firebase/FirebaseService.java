package com.littlebank.finance.global.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.littlebank.finance.domain.notification.domain.Notification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class FirebaseService {

    @Async
    public void sendNotification(Notification notification) {
        Message message = Message.builder()
                .setToken(notification.getReceiver().getFcmToken())
                .setNotification(
                        com.google.firebase.messaging.Notification.builder()
                        .setTitle(notification.getMessage())
                        .build()
                )
                .build();
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("✅ FCM 전송 성공! 메시지 ID: " + response);
        } catch (FirebaseMessagingException e) {
            System.err.println("FCM 전송 실패: " + e.getMessage());
        }
    }
}
