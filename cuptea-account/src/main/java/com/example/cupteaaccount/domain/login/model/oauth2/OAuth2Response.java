package com.example.cupteaaccount.domain.login.model.oauth2;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public abstract class OAuth2Response {

    protected Map<String, Object> attributes;
    //제공자에서 발급해주는 아이디(번호)
    public abstract String getId();
    //이메일
    public abstract String getEmail();
    public abstract String getNickname();
    //사용자 실명 (설정한 이름)
    public abstract String getProfileUrl();
}
