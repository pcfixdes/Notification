package com.notification.test.notification.service;

import com.notification.test.notification.model.Notification;

public interface NotificationChannel {
    void send(Notification notification);
}
