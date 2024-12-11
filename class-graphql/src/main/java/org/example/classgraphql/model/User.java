package org.example.classgraphql.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private Long id;
    private String name;
    private String email;
    private String passwordHash;

    public User(Long userId, String name, String email) {
        this.id = userId;
        this.name = name;
        this.email = email;
    }
}
