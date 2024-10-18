package com.notification.test.notification.service;

import com.notification.test.notification.model.User;
import com.notification.test.notification.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignUp_Success() {
        User newUser = new User();
        newUser.setUsername("johndoe");
        newUser.setPassword(Base64.getEncoder().encodeToString("password".getBytes()));

        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.empty());
        when(userRepository.save(newUser)).thenReturn(newUser);

        User result = userService.signUp(newUser);
        assertEquals(newUser, result);
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    public void testSignUp_UserExists() {
        User existingUser = new User();
        existingUser.setUsername("johndoe");

        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(existingUser));

        assertThrows(IllegalArgumentException.class, () -> userService.signUp(existingUser));
    }

    @Test
    public void testLogin_Success() {
        User user = new User();
        user.setUsername("johndoe");
        user.setPassword(Base64.getEncoder().encodeToString("password".getBytes()));

        String encodedPassword = Base64.getEncoder().encodeToString("password".getBytes());

        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));

        User result = userService.login("johndoe", encodedPassword);
        assertEquals(user, result);
    }

    @Test
    public void testLogin_UserNotFound() {
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.login("johndoe", "encodedPassword"));
    }

    @Test
    public void testLogin_InvalidPassword() {
        User user = new User();
        user.setUsername("johndoe");
        user.setPassword(Base64.getEncoder().encodeToString("password".getBytes()));

        String encodedWrongPassword = Base64.getEncoder().encodeToString("wrongPassword".getBytes());

        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> userService.login("johndoe", encodedWrongPassword));
    }

    @Test
    public void testGetUserSubscribedCategories() {
        when(userRepository.findSubscribedCategoriesByUsername("johndoe"))
                .thenReturn(List.of("Sports", "Finance"));

        List<String> categories = userService.getUserSubscribedCategories("johndoe");
        assertEquals(List.of("Sports", "Finance"), categories);
        verify(userRepository, times(1)).findSubscribedCategoriesByUsername("johndoe");
    }

    @Test
    public void testGetUserNotificationChannels() {
        when(userRepository.findNotificationChannelsByUsername("johndoe"))
                .thenReturn(List.of("Email", "SMS"));

        List<String> channels = userService.getUserNotificationChannels("johndoe");
        assertEquals(List.of("Email", "SMS"), channels);
        verify(userRepository, times(1)).findNotificationChannelsByUsername("johndoe");
    }
}
