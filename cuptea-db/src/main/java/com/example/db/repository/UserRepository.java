package com.example.db.repository;

import com.example.db.domain.model.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID>{

    UserEntity findByLoginId(String loginId);

    Optional<UserEntity> findByLoginIdAndPassword(String loginId, String password);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByLoginIdAndEmail(String loginId, String email);

}
