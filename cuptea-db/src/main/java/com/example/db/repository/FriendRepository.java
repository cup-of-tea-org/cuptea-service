package com.example.db.repository;

import com.example.db.domain.model.entity.friend.FriendEntity;
import com.example.db.repository.querydsl.CustomFriendRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FriendRepository extends JpaRepository<FriendEntity, UUID>, CustomFriendRepository {

}