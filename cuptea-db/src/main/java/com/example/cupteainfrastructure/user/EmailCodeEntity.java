package com.example.cupteainfrastructure.user;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@RedisHash(value = "email-code", timeToLive = 60 * 3L)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailCodeEntity {

    @Id
    private UUID id;
    private String code;


}
