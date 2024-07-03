package com.example.db.repository.redis;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface EmailCodeRedisRepository extends CrudRepository<EmailCodeEntity, UUID> {
}
