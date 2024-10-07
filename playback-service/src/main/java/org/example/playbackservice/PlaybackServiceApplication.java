package org.example.playbackservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class PlaybackServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlaybackServiceApplication.class, args);
    }

}
