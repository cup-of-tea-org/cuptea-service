package com.example.db.domain.model.entity.user.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Interest {

    TRAVEL("여행"), SPORTS("운동"), CAFE("카페"), NONE("없음");

    private String description;

    Interest(String description) {
        this.description = description;
    }

}
