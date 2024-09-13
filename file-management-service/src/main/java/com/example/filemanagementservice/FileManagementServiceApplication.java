package com.example.filemanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FileManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileManagementServiceApplication.class, args);
    }

}
