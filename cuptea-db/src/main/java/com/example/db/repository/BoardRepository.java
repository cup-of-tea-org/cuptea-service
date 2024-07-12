package com.example.db.repository;

import com.example.db.domain.model.entity.board.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BoardRepository extends JpaRepository<BoardEntity, UUID> {
}
