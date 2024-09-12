package com.example.userservice.domain.user.controller;

import com.example.userservice.domain.user.dto.PasswordChangeDto;
import com.example.userservice.domain.user.dto.UserDto;
import com.example.userservice.domain.user.entity.User;
import com.example.userservice.domain.user.entity.UserLoginHistory;
import com.example.userservice.domain.user.exception.DuplicateUserException;
import com.example.userservice.domain.user.exception.UnauthorizedException;
import com.example.userservice.domain.user.exception.UserNotFoundException;
import com.example.userservice.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
        try {
            User user = userService.createUser(userDto.getName(), userDto.getEmail(), userDto.getPassword());
            return ResponseEntity.ok(user);
        } catch (DuplicateUserException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        User user = userService.updateUser(userId, userDto.getName(), userDto.getEmail());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}/login-histories")
    public ResponseEntity<List<UserLoginHistory>> getUserLoginHistories(@PathVariable Long userId) {
        List<UserLoginHistory> userLoginHistories = userService.getUserLoginHistories(userId);
        return ResponseEntity.ok(userLoginHistories);
    }

    @PostMapping("/{userId}/password-change")
    public ResponseEntity<Void> changePassword(@PathVariable Long userId, @RequestBody PasswordChangeDto passwordChangeDto) {
        try {
            userService.changePassword(userId, passwordChangeDto.getOldPassword(), passwordChangeDto.getNewPassword());
            return ResponseEntity.ok().build();
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}
