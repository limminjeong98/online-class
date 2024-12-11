package org.example.classgraphql.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginHistory implements Serializable {
    private Long id;
    private String userId;
    private String loginTime;
    private String ipAddress;
}
