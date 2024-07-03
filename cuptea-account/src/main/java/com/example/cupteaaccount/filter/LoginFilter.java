package com.example.cupteaaccount.filter;

import com.example.cupteaaccount.config.security.user.CustomUserDetails;
import com.example.cupteaaccount.domain.login.exception.UserNotFoundException;
import com.example.cupteaaccount.domain.token.controller.model.TokenResponse;
import com.example.cupteaaccount.domain.token.jwt.JwtHelper;
import com.example.cupteaaccount.filter.model.FilterErrorResponse;
import com.example.cupteaaccount.filter.model.LoginFilterRequest;
import com.example.db.domain.model.entity.user.UserEntity;
import com.example.db.domain.model.entity.user.enums.UserRole;
import com.example.db.repository.JoinUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

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

        String username = null;
        String rawPassword = null;
        try {
            LoginFilterRequest loginFilterRequest = objectMapper.readValue(
                    request.getReader().lines().collect(Collectors.joining()), LoginFilterRequest.class);

            log.info("loginId = {}", loginFilterRequest.getLoginId());
            log.info("pw = {}", loginFilterRequest.getPassword());

            username = loginFilterRequest.getLoginId();
            rawPassword = loginFilterRequest.getPassword();

        } catch (IOException e) {
            log.error("", e);
        }

        UserEntity findUser = joinUserRepository.findByLoginId(username);

        if (findUser == null || (!passwordEncoder.matches(rawPassword, findUser.getPassword()))) {
            try {
                response.setStatus(400);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(
                        objectMapper.writeValueAsString(
                                new FilterErrorResponse("아이디 또는 비밀번호가 맞지 않습니다.")));
                return null;
            } catch (IOException e) {
                log.error("", e.getMessage());
            }
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
        try {
            response.setStatus(401);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(
                    objectMapper.writeValueAsString(objectMapper.writeValueAsString(
                            new FilterErrorResponse("아이디 또는 비밀번호가 맞지 않습니다.")
                    ))
            );

        }catch (IOException e) {
            response.setStatus(401);
            log.error("", "JSON PARSE EXCEPTION");
        }


        log.error("로그인 실패");
    }
}
