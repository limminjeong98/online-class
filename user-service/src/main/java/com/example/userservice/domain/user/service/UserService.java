package com.example.userservice.domain.user.service;

import com.example.userservice.domain.user.entity.User;
import com.example.userservice.domain.user.entity.UserLoginHistory;
import com.example.userservice.domain.user.repository.UserLoginHistoryRepository;
import com.example.userservice.domain.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserLoginHistoryRepository userLoginHistoryRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserLoginHistoryRepository userLoginHistoryRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userLoginHistoryRepository = userLoginHistoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(String name, String email, String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUser(Long userId, String name, String email) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(name);
        user.setEmail(email);
        return userRepository.save(user);
    }

    public List<UserLoginHistory> getUserLoginHistories(Long userId) {
        return userRepository.findById(userId)
                .map(User::getLoginHistories)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void logUserLogin(User user, String ipAddress) {
        UserLoginHistory userLoginHistory = new UserLoginHistory();
        userLoginHistory.setUser(user);
        userLoginHistory.setIpAddress(ipAddress);
        userLoginHistory.setLoginTime(LocalDateTime.now());
        userLoginHistoryRepository.save(userLoginHistory);
    }

    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            user.setPasswordHash(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Invalid old password");
        }
    }
}
