package com.example.cupteainfrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class CupteaInfrastructureApplication {

    public static void main(String[] args) {
        SpringApplication.run(CupteaInfrastructureApplication.class, args);
    }

}
