package com.example.userservice.domain.user.service;

import com.example.userservice.domain.user.entity.User;
import com.example.userservice.domain.user.repository.UserLoginHistoryRepository;
import com.example.userservice.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserLoginHistoryRepository userLoginHistoryRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser() {
        // given
        String name = "John Doe";
        String email = "john@example.com";
        String password = "password123";

        User user = new User();
        user.setName(name);
        user.setEmail(email);

        // when
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        User savedUser = userService.createUser(name, email, password);

        // then
        assertNotNull(savedUser);
        assertEquals(name, savedUser.getName());
        assertEquals(email, savedUser.getEmail());
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(password);
    }

    @Test
    void getUserByID_found() {
        // given
        Long userId = 1L;
        Optional<User> user = Optional.of(new User());

        // when
        when(userRepository.findById(userId)).thenReturn(user);
        Optional<User> result = userService.getUserById(userId);

        // then
        assertTrue(result.isPresent());
        verify(userRepository).findById(userId);
    }

    @Test
    void updateUser_success() {
        // given
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("Original Name");
        user.setEmail("original@example.com");

        // when
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        User result = userService.updateUser(userId, "Updated Name", "updated@example.com");

        // then
        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("updated@example.com", result.getEmail());
        verify(userRepository).findById(userId);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void changePassword_success() {
        // given
        Long userId = 1L;
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        User user = new User();
        user.setPasswordHash("encodedPassword");

        // when
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(oldPassword, user.getPasswordHash())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        // then
        assertDoesNotThrow(() -> userService.changePassword(userId, oldPassword, newPassword));
        verify(passwordEncoder).encode(newPassword);
        verify(userRepository).save(user);
    }

    @Test
    void changePassword_failWithWrongOldPassword() {
        // given
        Long userId = 1L;
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        User user = new User();
        user.setPasswordHash("encodedPassword");

        // when
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(oldPassword, user.getPasswordHash())).thenReturn(false);

        // then
        assertThrows(IllegalArgumentException.class, () -> userService.changePassword(userId, oldPassword, newPassword));
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
}