package com.notification.test.notification.repository;

import com.notification.test.notification.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("SELECT u.subscribedCategories FROM User u WHERE u.username = ?1")
    List<String> findSubscribedCategoriesByUsername(String username);

    @Query("SELECT u.notificationChannels FROM User u WHERE u.username = ?1")
    List<String> findNotificationChannelsByUsername(String username);

}
