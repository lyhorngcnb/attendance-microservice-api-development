package com.lyhorng.propertyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PropertyserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PropertyserviceApplication.class, args);
	}

}
