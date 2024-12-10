package org.example.classgraphql.service;

import lombok.extern.slf4j.Slf4j;
import org.example.classgraphql.model.User;
import org.example.classgraphql.service.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final RestTemplate restTemplate;

    private static final String BASE_URL = "http://user-service/users";

    @Autowired
    public UserService(@LoadBalanced RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public User createUser(String name, String email, String password) {
        UserDTO userDTO = new UserDTO(name, email, password);
        return restTemplate.postForObject(BASE_URL, userDTO, User.class);
    }

    public Optional<User> findById(Long userId) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/{userId}")
                .buildAndExpand(userId)
                .toUriString();

        return Optional.ofNullable(restTemplate.getForObject(url, User.class));
    }

    public User updateUser(Long userId, String name, String email) {
        UserDTO userDTO = new UserDTO(name, email, null);
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/{userId}")
                .buildAndExpand(userId)
                .toUriString();

        restTemplate.put(url, userDTO);

        return new User(userId, name, email);
    }
}
