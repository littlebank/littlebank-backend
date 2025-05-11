package com.littlebank.finance.domain.notification.domain.repository;

import com.littlebank.finance.domain.notification.domain.Notification;
import com.littlebank.finance.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByReceiverOrderByCreatedDateDesc(User user, Pageable pageable);
}
