package com.example.cupteaaccount.domain.token.jwt;

import com.example.cupteaaccount.domain.token.controller.model.TokenDto;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class JwtHelper {

    private SecretKey secretKey;

    // create algorithm
    public JwtHelper(@Value("${spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts
                        .SIG
                        .HS256
                        .key()
                        .build()
                        .getAlgorithm());
    }

    // role parse
    public String getRole(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    // getNickname
    public String getNickname(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("nickname", String.class);
    }

    // getID
    public UUID getID(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("id", UUID.class);
    }

    // 만료 시간
    public Boolean isExpired(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    // jwt 생성
    public String createToken(UUID id, String nickname, String role, Long expiredMs) {
        String token = Jwts.builder()
                .claim("id", id)
                .claim("nickname", nickname)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();

        log.info("token 생성 : {}", token);

        return token;
    }




}
