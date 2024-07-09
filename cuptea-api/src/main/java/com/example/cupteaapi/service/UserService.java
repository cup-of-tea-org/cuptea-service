package com.example.cupteaapi.service;

import com.example.cupteaapi.exceptionhandler.exception.UserNotFoundException;
import com.example.db.domain.model.dto.user.UserResponseDto;
import com.example.db.domain.model.entity.user.UserEntity;
import com.example.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResponseDto getUser() {

        // ThreadLocal 에서 유저 정보 추출
        UUID userId = (UUID) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes().getAttribute("userId", RequestAttributes.SCOPE_REQUEST));

        // 유저 DB에서 찾기
        // 컵티, 방명록, 친구 등 집계 쿼리 querydsl 로 뽑아내야함.
        UserEntity findUser =
                userRepository.findById(userId)
                .orElseThrow(
                        () -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        return getUserDto(findUser);
    }

    // UserEntity -> UserResponseDto 변환
    private UserResponseDto getUserDto(final UserEntity findUser) {
        return UserResponseDto.builder()
                .loginId(findUser.getLoginId())
                .email(findUser.getEmail())
                .phone(findUser.getPhone())
                .interest(findUser.getInterest())
                .birthday(findUser.getBirthday())
                .build();
    }
}
