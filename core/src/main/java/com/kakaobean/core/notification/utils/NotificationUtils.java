package com.kakaobean.core.notification.utils;

import com.kakaobean.core.notification.domain.Notification;

public class NotificationUtils {

    private NotificationUtils() {}

    public static String makeNotificationContent(String projectTitle, String content) {
        return "[ " + projectTitle + " ] " + content;
    }
}
