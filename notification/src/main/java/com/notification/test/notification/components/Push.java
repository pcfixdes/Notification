package com.notification.test.notification.components;

import com.notification.test.notification.model.Notification;
import com.notification.test.notification.service.NotificationChannel;
import org.springframework.stereotype.Component;

@Component
public class Push implements NotificationChannel {
    @Override
    public void send(Notification notification) {
    }
}

