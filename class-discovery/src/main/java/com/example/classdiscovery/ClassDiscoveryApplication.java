package com.example.classdiscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ClassDiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClassDiscoveryApplication.class, args);
	}

}
