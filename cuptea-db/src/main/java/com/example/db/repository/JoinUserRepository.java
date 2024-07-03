package com.example.db.repository;

import com.example.db.domain.model.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JoinUserRepository extends JpaRepository<UserEntity, UUID> {

    UserEntity findByLoginId(String loginId);

    UserEntity findByEmail(String email);

}
