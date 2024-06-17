package com.example.cupteaaccount.domain.login.service;

import com.example.cupteaaccount.domain.login.model.oauth2.CustomOAuth2User;
import com.example.cupteaaccount.domain.login.model.oauth2.OAuthAttributes;
import com.example.db.user.UserEntity;
import com.example.db.user.enums.SocialType;
import com.example.db.user.enums.UserRole;
import com.example.db.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        log.info("Login OAuth2User : {}", oAuth2User);

        // 어디 플랫폼 Kakao || Google ??
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // 소셜 타입 받아와서
        SocialType socialType = getSocialType(registrationId);
        log.info("socialType : {}", socialType);

        // oAuth2 로그인 시 키가 되는 값
        String usernameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // API 정보
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuthAttributes oAuthAttributes = new OAuthAttributes();
        oAuthAttributes = oAuthAttributes.of(socialType, usernameAttributeName, attributes);


        UserEntity createdUser = getUser(oAuthAttributes, socialType);

        return new CustomOAuth2User(
                Collections.singletonList(new SimpleGrantedAuthority(createdUser.getRole().toString())),
                attributes,
                oAuthAttributes.getNameAttributeKey(),
                createdUser.getId(),
                createdUser.getLoginId(),
                createdUser.getRole(),
                createdUser.getEmail()
        );
    }

    private SocialType getSocialType(String registrationId) {
        if (registrationId.equals("kakao")) {
            return SocialType.KAKAO;
        } else if (registrationId.equals("google")) {
            return SocialType.GOOGLE;
        }
        return SocialType.NONE;

    }

    @Transactional
    public UserEntity getUser(OAuthAttributes oAuthAttributes, SocialType socialType) {
        UserEntity user = userRepository.findByLoginId(oAuthAttributes.getOAuth2Response().getNickname());

        if (user == null) {
            return createUser(oAuthAttributes, socialType);
        }

        return user;
    }

    private UserEntity createUser(OAuthAttributes oAuthAttributes, SocialType socialType) {
        UserEntity user =  UserEntity.builder()
                .loginId(oAuthAttributes.getOAuth2Response().getId())
                .password(null)
                .phone(null)
                .email(oAuthAttributes.getOAuth2Response().getEmail())
                .birthday(null)
                .socialType(socialType)
                .role(UserRole.USER)
                .profileImgName(oAuthAttributes.getOAuth2Response().getProfileUrl())
                .build();

        return userRepository.save(user);
    }
}
