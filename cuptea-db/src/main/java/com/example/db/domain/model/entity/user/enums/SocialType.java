package com.example.db.domain.model.entity.user.enums;

public enum SocialType {

    KAKAO("카카오"), GOOGLE("구글"),NONE("소셜 로그인 아님")
    ;

    private String description;

    SocialType(String description) {
        this.description = description;
    }
}
