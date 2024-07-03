package com.example.db.domain.model.entity.user.enums;

public enum UserRole {

    USER("유저"), ADMIN("관리자");

    private String description;

    UserRole(String description) {
        this.description = description;
    }
}
