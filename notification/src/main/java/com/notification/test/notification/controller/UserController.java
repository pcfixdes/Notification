package com.notification.test.notification.controller;

import com.notification.test.notification.model.User;
import com.notification.test.notification.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody User user) {
        try {
            User signedUpUser = userService.signUp(user);
            return ResponseEntity.ok(signedUpUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<HttpStatus> login(@RequestParam String username, @RequestParam String encodedPassword) {
        try {
            User user = userService.login(username, encodedPassword);
            if(user!=null){
                return ResponseEntity.ok(HttpStatus.OK);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{username}/subscribed-categories")
    public ResponseEntity<List<String>> getSubscribedCategories(@PathVariable String username) {
        List<String> categories = userService.getUserSubscribedCategories(username);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{username}/notification-channels")
    public ResponseEntity<List<String>> getNotificationChannels(@PathVariable String username) {
        List<String> channels = userService.getUserNotificationChannels(username);
        return ResponseEntity.ok(channels);
    }
}
