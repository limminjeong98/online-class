package org.example.classgraphql.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginHistory {
    private Long id;
    private String userId;
    private String loginTime;
    private String ipAddress;
}
