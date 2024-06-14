package com.example.cupteaaccount.domain.login.model.oauth2;

import java.util.Map;

public class GoogleOAuth2UserDto extends OAuth2Response{
    public GoogleOAuth2UserDto(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("sub"));
    }

    @Override
    public String getEmail() {
        return String.valueOf(attributes.get("email"));
    }

    @Override
    public String getNickname() {
        return String.valueOf(attributes.get("given_name"));
    }

    @Override
    public String getProfileUrl() {
        return String.valueOf(attributes.get("picture"));
    }
}
