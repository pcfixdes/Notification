package com.notification.test.notification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification.test.notification.model.User;
import com.notification.test.notification.service.UserService;
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

public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSignUp_Success() throws Exception {
        User user = new User();
        user.setUsername("johndoe");
        user.setPassword("password");

        when(userService.signUp(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("johndoe"));
    }

    @Test
    public void testSignUp_Failure() throws Exception {
        User user = new User();
        user.setUsername("invalid");

        when(userService.signUp(any(User.class))).thenThrow(new IllegalArgumentException());

        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLogin_Success() throws Exception {
        when(userService.login("johndoe", "password")).thenReturn(new User());

        mockMvc.perform(post("/api/users/login")
                        .param("username", "johndoe")
                        .param("encodedPassword", "password"))
                .andExpect(status().isOk());
    }

    @Test
    public void testLogin_NotFound() throws Exception {
        when(userService.login("unknown", "password")).thenThrow(new IllegalArgumentException());

        mockMvc.perform(post("/api/users/login")
                        .param("username", "unknown")
                        .param("encodedPassword", "password"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetSubscribedCategories_Success() throws Exception {
        List<String> categories = List.of("news", "updates");

        when(userService.getUserSubscribedCategories("johndoe")).thenReturn(categories);

        mockMvc.perform(get("/api/users/johndoe/subscribed-categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("news"))
                .andExpect(jsonPath("$[1]").value("updates"));
    }

    @Test
    public void testGetNotificationChannels_Success() throws Exception {
        List<String> channels = List.of("email", "sms");

        when(userService.getUserNotificationChannels("johndoe")).thenReturn(channels);

        mockMvc.perform(get("/api/users/johndoe/notification-channels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("email"))
                .andExpect(jsonPath("$[1]").value("sms"));
    }
}
