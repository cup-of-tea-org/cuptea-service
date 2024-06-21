package com.example.cupteaaccount.filter;

import com.example.cupteaaccount.config.security.user.CustomUserDetails;
import com.example.cupteaaccount.domain.login.exception.UserNotFoundException;
import com.example.cupteaaccount.domain.token.controller.model.TokenResponse;
import com.example.cupteaaccount.domain.token.jwt.JwtHelper;
import com.example.db.user.UserEntity;
import com.example.db.user.enums.UserRole;
import com.example.db.user.repository.JoinUserRepository;
import com.example.db.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;
    private final PasswordEncoder passwordEncoder;
    private final JoinUserRepository joinUserRepository;

    private final ObjectMapper objectMapper;

    public LoginFilter(
            AuthenticationManager authenticationManager,
            JwtHelper jwtHelper,
            PasswordEncoder passwordEncoder,
            JoinUserRepository joinUserRepository,
            ObjectMapper objectMapper

    ) {
        this.authenticationManager = authenticationManager;
        this.jwtHelper = jwtHelper;
        this.passwordEncoder = passwordEncoder;
        this.joinUserRepository = joinUserRepository;
        this.objectMapper = objectMapper;
        setFilterProcessesUrl("/open-api/login"); // 필터 경로 설정
        setPostOnly(true);

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = obtainUsername(request);
        String rawPassword = obtainPassword(request);

        final UserEntity findUser = Objects.requireNonNull(joinUserRepository.findByLoginId(username), () -> {
            throw new UserNotFoundException("유저 찾기 실패");
        });

        // 비밀번호 검증
        if (!passwordEncoder.matches(rawPassword, findUser.getPassword())) {
            throw new UserNotFoundException("비밀번호 불일치");
        }

        UserEntity user = UserEntity.builder()
            .loginId(findUser.getLoginId())
            .password(findUser.getPassword())
            .phone(findUser.getPhone())
            .email(findUser.getEmail())
            .birthday(findUser.getBirthday())
            .profileImgName(findUser.getProfileImgName())
            .role(findUser.getRole())
            .socialType(findUser.getSocialType())
                .interest(findUser.getInterest())
            .build();

    CustomUserDetails customUserDetails = new CustomUserDetails(user);

    UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(
                    username, rawPassword, null);

    SecurityContextHolder.getContext().setAuthentication(authToken);


    return authenticationManager.authenticate(authToken);
}

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        // 유저 정보
        String username = authResult.getName();
        String password = obtainPassword(request);
        log.info("password = {}", password);

        log.info("username = {}", username);

        final UserEntity findUser = Objects.requireNonNull(joinUserRepository.findByLoginId(username), () -> {
            throw new UserNotFoundException("유저 찾기 실패");
        });

        authResult.getAuthorities().stream().findFirst().ifPresent(grantedAuthority -> {
            UserRole role = UserRole.valueOf(grantedAuthority.getAuthority());
            log.info("userRole = {}", role);
            String accessToken = jwtHelper.createToken(findUser.getId(), username, role, 1800000L); // 30분

            String refreshToken = jwtHelper.createToken(findUser.getId(), username, role, 86400000L); // 1day

            // refresh 토큰은 DB에 저장
            findUser.setRefreshToken(refreshToken);

            // client 에게 token body 로 전달
            TokenResponse tokenResponse = TokenResponse.builder()
                    .token(accessToken)
                    .build();

            try {
                response.getWriter().write(objectMapper.writeValueAsString(tokenResponse));
                log.info("로그인 성공");
            } catch (IOException e) {
                log.error("[LoginFilter()] token json stringify 오류 >>>>>>>>>>>>>>>>>>>");
                log.error("", e);
            }
        });
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(401);
        log.error("로그인 실패");
    }
}
