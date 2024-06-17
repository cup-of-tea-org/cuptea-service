package com.example.db.user.repository;

import com.example.db.user.EmailCodeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface EmailCodeRedisRepository extends CrudRepository<EmailCodeEntity, UUID> {
}
