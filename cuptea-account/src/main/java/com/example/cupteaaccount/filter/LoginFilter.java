package com.example.cupteaaccount.filter;

import com.example.cupteaaccount.domain.login.exception.UserNotFoundException;
import com.example.cupteaaccount.domain.token.jwt.JwtHelper;
import com.example.cupteainfrastructure.user.UserEntity;
import com.example.cupteainfrastructure.user.repository.JoinUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
            JoinUserRepository joinUserRepository, ObjectMapper objectMapper

    ) {
        this.authenticationManager = authenticationManager;
        this.jwtHelper = jwtHelper;
        this.passwordEncoder = passwordEncoder;
        this.joinUserRepository = joinUserRepository;
        this.objectMapper = objectMapper;
        setFilterProcessesUrl("/open-api/login"); // 필터 경로 설정
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        final String username = obtainUsername(request);
        String rawPassword = obtainPassword(request);

        // 암호화후 Authentication 객체에 저장
        final String password = passwordEncoder.encode(rawPassword);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

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
            String role = grantedAuthority.getAuthority();
            String accessToken = jwtHelper.createToken(findUser.getId(), username, role, 1800000L); // 30분
            // refresh 토큰은 DB에 저장
            String refreshToken = jwtHelper.createToken(findUser.getId(), username, role, 86400000L); // 1day


            // client 에게 token body 로 전달
            try {
                response.getWriter().write(objectMapper.writeValueAsString(accessToken));
            } catch (IOException e) {
                log.error("[LoginFilter()] token json stringify 오류 >>>>>>>>>>>>>>>>>>>");
                log.error("", e);
            }
        });
    }
}
