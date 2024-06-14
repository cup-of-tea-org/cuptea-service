package com.example.cupteainfrastructure.user.repository;

import com.example.cupteainfrastructure.user.EmailCodeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface EmailCodeRedisRepository extends CrudRepository<EmailCodeEntity, UUID> {
}
