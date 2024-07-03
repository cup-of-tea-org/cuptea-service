package com.example.cupteaaccount.domain.login.model.oauth2;

import com.example.db.domain.model.entity.user.enums.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private UUID userId;
    private UserRole userRole;
    private String nickname;
    private String email;


    public CustomOAuth2User(
            Collection<? extends GrantedAuthority> authorities,
            Map<String, Object> attributes,
            String nameAttributeKey,
            UUID userId,
            String nickname,
            UserRole userRole,
            String email
            ) {
        super(authorities, attributes, nameAttributeKey);
        this.userId = userId;
        this.nickname = nickname;
        this.userRole = userRole;
        this.email = email;
    }
}
