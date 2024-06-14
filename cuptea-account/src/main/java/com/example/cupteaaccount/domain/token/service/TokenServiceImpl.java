package com.example.cupteaaccount.domain.token.service;

import com.example.cupteaaccount.domain.token.controller.model.TokenDto;
import com.example.cupteaaccount.domain.token.exception.TokenNotFoundException;
import com.example.cupteaaccount.domain.token.jwt.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtHelper jwtHelper;

    @Override
    public UUID validateToken(final TokenDto tokenDto) {
        final Boolean isTokenValid = this.isTokenExpired(tokenDto.getToken());
        if (isTokenValid) {
            return jwtHelper.getID(tokenDto.getToken());
        } else
            throw new TokenNotFoundException("Token is not valid");
    }

    private Boolean isTokenExpired(String token) {
        return jwtHelper.isExpired(token);
    }
}
