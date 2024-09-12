package com.example.userservice.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeDto {
    private String oldPassword;
    private String newPassword;
}