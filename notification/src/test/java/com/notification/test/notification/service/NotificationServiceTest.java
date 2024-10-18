package com.notification.test.notification.service;

import com.notification.test.notification.model.Notification;
import com.notification.test.notification.model.User;
import com.notification.test.notification.repository.NotificationRepository;
import com.notification.test.notification.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationChannel emailChannel;

    private Map<String, NotificationChannel> channels;

    private NotificationService notificationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize the map of channels with the mocked emailChannel
        List<NotificationChannel> channelList = List.of(emailChannel);
        channels = channelList.stream().collect(Collectors.toMap(c -> c.getClass().getSimpleName(), c -> c));

        notificationService = new NotificationService(channelList, notificationRepository, userRepository);
    }



    @Test
    public void testProcessNotification_NoChannel() {
        Notification notification = new Notification();
        notification.setChannel("Unknown");

        assertThrows(IllegalArgumentException.class, () -> notificationService.processNotification(notification));
    }

    @Test
    public void testGetNotificationsForUser_Success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("johndoe");

        Notification notification = new Notification();
        notification.setUserId(1L);

        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));
        when(notificationRepository.findByUserId(1L)).thenReturn(List.of(notification));

        List<Notification> notifications = notificationService.getNotificationsForUser("johndoe");
        assertEquals(1, notifications.size());
        assertEquals(notification, notifications.get(0));
    }

    @Test
    public void testGetNotificationsForUser_UserNotFound() {
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> notificationService.getNotificationsForUser("johndoe"));
    }
}
