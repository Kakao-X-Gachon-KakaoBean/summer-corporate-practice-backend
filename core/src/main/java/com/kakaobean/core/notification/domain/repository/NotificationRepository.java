package com.kakaobean.core.notification.domain.repository;

import com.kakaobean.core.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface NotificationRepository extends JpaRepository <Notification, Long> {

    @Modifying
    @Transactional
    @Query("update Notification n set n.hasRead = true where n.id = :notificationId")
    void modifyReadStatus(Long notificationId);
}
