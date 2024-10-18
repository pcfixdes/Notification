package com.notification.test.notification.service;

import com.notification.test.notification.model.User;
import com.notification.test.notification.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User signUp(User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User with this username already exists.");
        }
        return userRepository.save(user);
    }

    public User login(String username, String encodedPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        String decodedPassword = new String(Base64.getDecoder().decode(encodedPassword));
        String decodedUserPassword = new String(Base64.getDecoder().decode(user.getPassword()));


        if (!decodedUserPassword.equals(decodedPassword)) {
            throw new IllegalArgumentException("Invalid password.");
        }

        return user;
    }

    public List<String> getUserSubscribedCategories(String username) {
        return userRepository.findSubscribedCategoriesByUsername(username);
    }

    public List<String> getUserNotificationChannels(String username) {
        return userRepository.findNotificationChannelsByUsername(username);
    }
}
