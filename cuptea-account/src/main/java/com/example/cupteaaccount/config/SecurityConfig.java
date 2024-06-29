package com.example.cupteaaccount.config;

import com.example.cupteaaccount.config.security.handler.CustomFailHandler;
import com.example.cupteaaccount.config.security.handler.CustomSuccessHandler;
import com.example.cupteaaccount.domain.login.service.CustomOAuth2UserService;
import com.example.cupteaaccount.domain.token.jwt.JwtHelper;
import com.example.cupteaaccount.filter.LoginFilter;
import com.example.db.user.repository.JoinUserRepository;
import com.example.db.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final CustomFailHandler customFailHandler;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtHelper jwtHelper;
    private final JoinUserRepository joinUserRepository;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;



    private static final List<String> SWAGGER = List.of(
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    );

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        it -> it
                                .requestMatchers(SWAGGER.toArray(new String[0])).permitAll()
                                .requestMatchers("/open-api/**").permitAll()
                                .requestMatchers("/api/token/**").permitAll()
                                .requestMatchers("/oauth2/**").permitAll()
                                .requestMatchers("/api/refresh-token/**").permitAll()
                                .requestMatchers("/login/**").permitAll()
                                .anyRequest().authenticated()
                );

        http
                .httpBasic(AbstractHttpConfigurer::disable);

        http
                .formLogin(AbstractHttpConfigurer::disable);

        http
                .sessionManagement((session) -> {
                    session.
                            sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                });
        http
                .oauth2Login((oauth2) ->
                        oauth2.userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
                                        .userService(customOAuth2UserService)))
                                .successHandler(customSuccessHandler)
                                .failureHandler(customFailHandler)

                );

        // cors 설정 local
        http

                .cors((cors) -> {
                    cors.configurationSource(request -> {
                        // cors 설정
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("http://52.79.137.212:80"));
                        // config.setAllowedOrigins(Collections
//                        .singletonList("http://52.79.137.212:3000"));
                        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
                        config.setMaxAge(3600L);
                        return config;
                    });
                });
        // cors 설정 dev
//        http
//
//                .cors((cors) -> {
//                    cors.configurationSource(request -> {
//                        // cors 설정
//                        CorsConfiguration config = new CorsConfiguration();
//
//                        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//                        config.setAllowCredentials(true);
//                        config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
//                        config.setMaxAge(3600L);
//                        return config;
//                    });
//                });

        http
                .addFilterAt(new LoginFilter(
                                authenticationManager(authenticationConfiguration),
                                jwtHelper,
                                passwordEncoder(),
                                joinUserRepository,
                                objectMapper

                        ), UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }


    // BCrypt hash 암호화 사용
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
