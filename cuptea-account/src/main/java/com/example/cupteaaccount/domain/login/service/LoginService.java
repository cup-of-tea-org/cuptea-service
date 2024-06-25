package com.example.cupteaaccount.domain.login.service;

import com.example.cupteaaccount.common.model.Mail;
import com.example.cupteaaccount.domain.login.controller.model.dto.FindIdRequestDto;
import com.example.cupteaaccount.domain.login.controller.model.dto.FindIdResponseDto;
import com.example.cupteaaccount.domain.login.controller.model.dto.FindPasswordRequestDto;
import com.example.cupteaaccount.domain.login.exception.UserNotFoundException;
import com.example.cupteaaccount.util.MailHelper;
import com.example.db.user.EmailCodeEntity;
import com.example.db.user.UserEntity;
import com.example.db.user.repository.EmailCodeRedisRepository;
import com.example.db.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final UserRepository userRepository;
    private final MailHelper mailHelper;
    private final EmailCodeRedisRepository emailCodeRedisRepository;

    @Transactional(readOnly = true)
    public FindIdResponseDto findUserByEmail(final FindIdRequestDto findIdRequestDto) {

        final UserEntity findUser = userRepository.findByEmail(findIdRequestDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("해당 이메일로 가입된 사용자가 없습니다."));

        return FindIdResponseDto.builder()
                .loginId(findUser.getLoginId())
                .build();
    }

    @Transactional
    public void findPassword(final FindPasswordRequestDto findPasswordRequestDto) {
        final UserEntity findUser = userRepository.findByLoginIdAndEmail(findPasswordRequestDto.getLoginId(), findPasswordRequestDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("해당 이메일로 가입된 사용자가 없습니다."));

        // 이메일 생성 validate emailCode
        StringBuffer subject = new StringBuffer();
        subject
                .append("[CUP TEA]\n")
                .append("비밀번호 찾기 인증번호입니다.");

        UUID randomId = UUID.randomUUID();

        StringBuffer text = new StringBuffer();
        text
                .append("인증번호는 : ")
                .append(randomId + "\n")
                .append("사이트에 돌아가셔서 인증해주세요\n")
                .append("주의 ! : 5분 이내에 입력해주셔야 합니다!\n");

        mailHelper.createEmail(
                Mail.builder()
                        .setTo(findUser.getEmail())
                        .subject(subject.toString())
                        .text(text.toString())
                        .build()
        );
        log.info("이메일 전송 완료! [비밀번호 찾기]");

        // redis 에 이메일코드 저장
        emailCodeRedisRepository.save(EmailCodeEntity.builder()
                .id(randomId)
                .build());
    }

    @Transactional(readOnly = true)
    public void validateEmailCode(String emailCode) {
        emailCodeRedisRepository
                .findById(UUID.fromString(emailCode))
                .orElseThrow(() -> new UserNotFoundException("인증 코드가 존재하지 않습니다."));
    }
}
