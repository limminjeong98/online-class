package com.example.userservice.domain.user.controller;

import com.example.userservice.domain.user.dto.AuthRequest;
import com.example.userservice.domain.user.dto.TokenRequest;
import com.example.userservice.domain.user.entity.User;
import com.example.userservice.domain.user.service.JWTService;
import com.example.userservice.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JWTService jwtService;
    private final UserService userService;

    public AuthController(JWTService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> generateToken(HttpServletRequest request, @RequestBody AuthRequest authRequest) {
        User existingUser = userService.getUserByEmail(authRequest.getEmail()).orElseThrow();
        String token = jwtService.generateToken(existingUser, authRequest.getPassword());
        String ipAddress = request.getRemoteAddr();
        userService.logUserLogin(existingUser, ipAddress);
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

    @PostMapping("/verify-token")
    public ResponseEntity<Map<String, Boolean>> verifyToken(HttpServletRequest request, @RequestBody TokenRequest tokenRequest) {
        boolean isValid = jwtService.validateToken(tokenRequest.getToken());
        return ResponseEntity.ok(Collections.singletonMap("isValid", isValid));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshToken(HttpServletRequest request, @RequestBody TokenRequest tokenRequest) {
        String refreshToken = jwtService.refreshToken(tokenRequest.getToken());
        return ResponseEntity.ok(Collections.singletonMap("token", refreshToken));
    }
}
