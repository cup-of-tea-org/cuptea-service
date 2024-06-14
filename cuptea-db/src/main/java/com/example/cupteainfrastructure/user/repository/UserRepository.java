package com.example.cupteainfrastructure.user.repository;

import com.example.cupteainfrastructure.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID>{

    UserEntity findByLoginId(String loginId);
}
