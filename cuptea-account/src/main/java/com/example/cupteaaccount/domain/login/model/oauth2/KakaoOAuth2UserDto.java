package com.example.cupteaaccount.domain.login.model.oauth2;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class KakaoOAuth2UserDto extends OAuth2Response {

    public KakaoOAuth2UserDto(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getEmail() {
        return String.valueOf(attributes.get("email"));
    }

    @Override
    public String getNickname() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        log.info("profile: {}", profile);

        return profile == null ? null : (String) profile.get("nickname");
    }

    @Override
    public String getProfileUrl() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return profile == null ? null : String.valueOf(profile.get("thumbnail_image_url"));
    }
}
