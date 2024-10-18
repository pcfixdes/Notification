package com.notification.test.notification.service;

import com.notification.test.notification.model.Notification;
import com.notification.test.notification.model.User;
import com.notification.test.notification.repository.NotificationRepository;
import com.notification.test.notification.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final Map<String, NotificationChannel> channels;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    public NotificationService(List<NotificationChannel> channels, NotificationRepository notificationRepository, UserRepository userRepository) {
        this.channels = channels.stream().collect(Collectors.toMap(c -> c.getClass().getSimpleName(), Function.identity()));
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;

    }

    @Transactional
    public Notification processNotification(Notification notification) {
        NotificationChannel channel = channels.get(notification.getChannel());
        if (channel != null) {
            channel.send(notification);
            Notification savedNotification = notificationRepository.save(notification);
            if (savedNotification.getId() != null) {
                LoggerFactory.getLogger(NotificationService.class)
                        .info("Notification saved with ID: " + savedNotification.getId());
            }
        } else {
            throw new IllegalArgumentException("No channel found for: " + notification.getChannel());
        }
        return notification;
    }

    public List<Notification> getNotificationsForUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        return notificationRepository.findByUserId(user.getId());
    }
}
