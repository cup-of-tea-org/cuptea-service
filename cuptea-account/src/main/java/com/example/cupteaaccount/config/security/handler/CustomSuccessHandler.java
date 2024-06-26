package com.example.cupteaaccount.config.security.handler;

import com.example.cupteaaccount.domain.login.exception.UserNotFoundException;
import com.example.cupteaaccount.domain.login.model.oauth2.CustomOAuth2User;
import com.example.cupteaaccount.domain.token.controller.model.TokenDto;
import com.example.cupteaaccount.domain.token.jwt.JwtHelper;
import com.example.db.user.UserEntity;
import com.example.db.user.enums.UserRole;
import com.example.db.user.repository.JoinUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtHelper jwtHelper;
    private final ObjectMapper objectMapper;
    private final JoinUserRepository joinUserRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        log.info("oauth2 성공 user : {}", customOAuth2User.getNickname());

        // principal 객체에서 nickname 추출
        String nickname = customOAuth2User.getNickname();

        var auth = authentication.getAuthorities();
        UserRole role = UserRole.valueOf(auth.iterator().next().getAuthority());

        log.info("userRole = {}", role);


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
        String refreshToken = jwtHelper.createToken(
                customOAuth2User.getUserId(),
                nickname,
                role,
                86400000L // 하루 24시간
        );

        // loginId로 회원 정보 조회
        UserEntity findUser = Objects.requireNonNull(joinUserRepository.findByLoginId(customOAuth2User.getNickname()), () -> {
            throw new UserNotFoundException("회원 정보를 찾을 수 없습니다.");
        });

        // refresh token 저장
        findUser.setRefreshToken(refreshToken);


        try {
            response.sendRedirect(getUriComponentsBuilder(accessToken.getToken()));
        } catch (Exception e) {
            log.error("[CustomSuccessHandler()] token json stringify 오류  >>>>>>>>>>>>>>>>>>>");
            log.error("", e);
        }

    }

    private String getUriComponentsBuilder(String accessToken) {
        return UriComponentsBuilder.fromUriString("http://localhost:5173")
                .queryParam("token", accessToken)
                .build()
                .toUriString();
    }


}
