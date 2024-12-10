package org.example.classgraphql.controller;

import org.example.classgraphql.model.User;
import org.example.classgraphql.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @QueryMapping
    public User getUser(@Argument Long userId) {
        // throw new NotImplementedException();
        return userService.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @MutationMapping
    public User createUser(@Argument String name, @Argument String email, @Argument String password) {
        // throw new NotImplementedException();
        return userService.createUser(name, email, password);
    }

    @MutationMapping
    public User updateUser(@Argument Long userId, @Argument String name, @Argument String email) {
        // throw new NotImplementedException();
        return userService.updateUser(userId, name, email);
    }
}
