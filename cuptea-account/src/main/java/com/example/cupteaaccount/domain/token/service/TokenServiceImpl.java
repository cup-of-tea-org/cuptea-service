package com.example.cupteaaccount.domain.token.service;

import com.example.cupteaaccount.domain.login.exception.UserNotFoundException;
import com.example.cupteaaccount.domain.token.controller.model.TokenDto;
import com.example.cupteaaccount.domain.token.exception.TokenNotFoundException;
import com.example.cupteaaccount.domain.token.jwt.JwtHelper;
import com.example.db.domain.model.entity.user.UserEntity;
import com.example.db.repository.JoinUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtHelper jwtHelper;
    private final JoinUserRepository joinUserRepository;

    @Override
    public UUID validateToken(final TokenDto tokenDto) {
        // token expired 확인
        final Boolean isTokenExpired = this.isTokenExpired(tokenDto.getToken());

        if (!isTokenExpired) {
            return jwtHelper.getID(tokenDto.getToken());
        }

        throw new TokenNotFoundException("토큰이 만료되었습니다.");
    }

    private Boolean isTokenExpired(String token) {

        Boolean isTokenExpired = jwtHelper.isExpired(token);

        if (isTokenExpired) {
            this.isRefreshTokenExpired(token);
        }

        return false;
    }

    private void isRefreshTokenExpired(String token) {

        UUID findId = jwtHelper.getID(token);

        final UserEntity findUser = joinUserRepository.findById(findId).orElseThrow(() -> {
            throw new UserNotFoundException("유저를 찾을 수 없습니다.");
        });

        Boolean isRefreshTokenExpired = jwtHelper.isExpired(findUser.getRefreshToken());

        if (isRefreshTokenExpired) {
            throw new TokenNotFoundException("토큰이 만료되었습니다.");
        }
    }
}

