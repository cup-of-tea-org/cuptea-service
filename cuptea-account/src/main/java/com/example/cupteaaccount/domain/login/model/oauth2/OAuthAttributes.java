package com.example.cupteaaccount.domain.login.model.oauth2;

import com.example.cupteainfrastructure.user.enums.SocialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthAttributes {

    private String nameAttributeKey;
    private OAuth2Response oAuth2Response;

    public OAuthAttributes of(SocialType socialType, String nameAttributeKey, Map<String, Object> attributes) {
        if (socialType == SocialType.KAKAO) {
            return ofKakao(nameAttributeKey, attributes);
        } else if (socialType == SocialType.GOOGLE) {
            return ofGoogle(nameAttributeKey, attributes);
        }

        return null;
    }

    private OAuthAttributes ofGoogle(String nameAttributeKey, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(nameAttributeKey)
                .oAuth2Response(new GoogleOAuth2UserDto(attributes))
                .build();
    }

    private OAuthAttributes ofKakao(String nameAttributeKey, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(nameAttributeKey)
                .oAuth2Response(new KakaoOAuth2UserDto(attributes))
                .build();
    }
}
