package com.example.cupteaaccount.config.handler;

import com.example.cupteaaccount.domain.login.model.oauth2.CustomOAuth2User;
import com.example.cupteaaccount.domain.token.controller.model.TokenDto;
import com.example.cupteaaccount.domain.token.jwt.JwtHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtHelper jwtHelper;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        log.info("oauth2 성공 user : {}", customOAuth2User.getNickname());

        // principal 객체에서 nickname 추출
        String nickname = customOAuth2User.getNickname();

        var auth = authentication.getAuthorities();
        String role = auth.iterator().next().getAuthority();

        String token = jwtHelper.createToken(
                customOAuth2User.getUserId(),
                nickname,
                role,
                1800000L // 30분
        );

        // token 객체 생성
        TokenDto accessToken = TokenDto.builder()
                .token(token)
                .build();
        //TODO Redis 에 저장
        String refreshToken = jwtHelper.createToken(
                customOAuth2User.getUserId(),
                nickname,
                role,
                86400000L // 하루
        );

        try {
            response.getWriter().write(objectMapper.writeValueAsString(accessToken));
        } catch (Exception e) {
            log.error("[CustomSuccessHandler()] token json stringify 오류  >>>>>>>>>>>>>>>>>>>");
            log.error("", e);
        }

    }
}
