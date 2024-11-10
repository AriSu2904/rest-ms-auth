package com.service.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ServiceAuthorization {

	public static void main(String[] args) {
		SpringApplication.run(ServiceAuthorization.class, args);
	}
}
