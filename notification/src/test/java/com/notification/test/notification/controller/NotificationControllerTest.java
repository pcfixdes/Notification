package com.notification.test.notification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification.test.notification.model.Notification;
import com.notification.test.notification.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class NotificationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSendNotification_Success() throws Exception {
        Notification notification = new Notification();
        notification.setChannel("Email");
        notification.setMessage("Test message"); // Set the correct field name

        when(notificationService.processNotification(any(Notification.class))).thenReturn(notification);

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notification)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.channel").value("Email"))
                .andExpect(jsonPath("$.message").value("Test message")); // Use the correct field name
    }


    @Test
    public void testSendNotification_Failure() throws Exception {
        Notification notification = new Notification();
        notification.setChannel("Email");
        notification.setMessage("Test message");

        when(notificationService.processNotification(any(Notification.class))).thenThrow(new IllegalArgumentException());

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notification)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetNotifications_Success() throws Exception {
        Notification notification = new Notification();
        notification.setChannel("Email");
        notification.setMessage("Test message"); // Ensure correct field is set

        List<Notification> notifications = List.of(notification);

        when(notificationService.getNotificationsForUser("johndoe")).thenReturn(notifications);

        mockMvc.perform(get("/api/notifications/johndoe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].channel").value("Email"))
                .andExpect(jsonPath("$[0].message").value("Test message")); // Use correct field name
    }


    @Test
    public void testGetNotifications_NotFound() throws Exception {
        when(notificationService.getNotificationsForUser("unknownuser")).thenReturn(List.of());

        mockMvc.perform(get("/api/notifications/unknownuser"))
                .andExpect(status().isNotFound());
    }
}
