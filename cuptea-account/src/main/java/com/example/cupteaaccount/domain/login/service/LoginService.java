package com.example.cupteaaccount.domain.login.service;

import com.example.cupteaaccount.domain.login.controller.model.dto.FindIdRequestDto;
import com.example.cupteaaccount.domain.login.controller.model.dto.FindIdResponseDto;
import com.example.cupteaaccount.domain.login.exception.UserNotFoundException;
import com.example.db.user.UserEntity;
import com.example.db.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public FindIdResponseDto findUserByEmail(final FindIdRequestDto findIdRequestDto) {

        final UserEntity findUser = userRepository.findByEmail(findIdRequestDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("해당 이메일로 가입된 사용자가 없습니다."));

        return FindIdResponseDto.builder()
                .loginId(findUser.getLoginId())
                .build();
    }
}
