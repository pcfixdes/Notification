package com.notification.test.notification.controller;

import com.notification.test.notification.model.Notification;
import com.notification.test.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<Notification> sendNotification(@RequestBody Notification notification) {
        try {
            Notification result = notificationService.processNotification(notification);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{userName}")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable String userName) {
        List<Notification> notifications = notificationService.getNotificationsForUser(userName);
        if (notifications.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(notifications);
    }
}
