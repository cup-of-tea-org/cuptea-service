package com.example.db.user.repository;

import com.example.db.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JoinUserRepository extends JpaRepository<UserEntity, UUID> {

    UserEntity findByLoginId(String loginId);

    UserEntity findByEmail(String email);



}
