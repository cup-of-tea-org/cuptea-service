package com.example.cupteaapi.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EntityScan(basePackages = "com.example.db")
@EnableJpaRepositories(basePackages = "com.example.db")
@EnableRedisRepositories(basePackages = "com.example.db.repository.redis")
public class JpaConfig {
}
