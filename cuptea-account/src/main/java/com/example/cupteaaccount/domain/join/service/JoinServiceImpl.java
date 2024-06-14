package com.example.cupteaaccount.domain.join.service;

import com.example.cupteaaccount.domain.join.controller.model.dto.EmailRequestDto;
import com.example.cupteaaccount.domain.join.controller.model.dto.JoinIdOverlappedDto;
import com.example.cupteaaccount.domain.join.exception.MailSendFailException;
import com.example.cupteaaccount.domain.join.exception.UserJoinFailException;
import com.example.cupteaaccount.domain.join.controller.model.dto.JoinUserDto;
import com.example.cupteainfrastructure.user.UserEntity;
import com.example.cupteainfrastructure.user.enums.UserRole;
import com.example.cupteainfrastructure.user.repository.JoinUserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JoinServiceImpl implements JoinService {

    private final JoinUserRepository joinUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;


    @Override
    public Boolean join(final JoinUserDto joinUserDto) {

        // 기존 회원인지 검증
        if (joinUserRepository.findByLoginId(joinUserDto.getLoginId()) != null) {
            throw new UserJoinFailException("이미 가입된 회원입니다.");
        }

        final UserEntity user = UserEntity.builder()
                .loginId(joinUserDto.getLoginId())
                .password(passwordEncoder.encode(joinUserDto.getPassword()))
                .phone(joinUserDto.getPhone())
                .email(joinUserDto.getEmail())
                .birthday(joinUserDto.getBirthday())
                .profileImgName(joinUserDto.getProfileImgName())
                .socialType(joinUserDto.getSocialType())
                .role(UserRole.USER)
                .build();

        joinUserRepository.save(user);

        return true;
    }

    @Override
    public Boolean isIdOverlapped(final JoinIdOverlappedDto joinIdOverlappedDto) {
        UserEntity findUser = joinUserRepository.findByLoginId(joinIdOverlappedDto.getLoginId());
        if (findUser == null) {
            return true; // id 중복 x
        }

        return false; // id 중복 o
    }

    // 이메일 생성
    @Override
    public void sendEmail(final EmailRequestDto emailRequestDto) {
        log.info("emailRequestDto = {}", emailRequestDto.getEmail());
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        // 제목, 내용 생성
        StringBuffer subject = new StringBuffer();
        subject
                .append("[CUP TEA]\n")
                .append("이메일 인증을 위한 인증번호를 알려드립니다.\n");

        StringBuffer text = new StringBuffer();
        text
                .append("인증번호는 : ")
                .append(UUID.randomUUID())
                .append("입니다.\n")
                .append("사이트에 돌아가셔서 인증해주세요\n");

        // 이메일 전송
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false , "UTF-8");
            mimeMessageHelper.setTo(emailRequestDto.getEmail());
            mimeMessageHelper.setSubject(subject.toString());
            mimeMessageHelper.setText(text.toString());
            javaMailSender.send(mimeMessage);

            //TODO 이메일 인증 코드 저장 redis 에

            log.info("이메일 전송 완료! [회원가입]");

        } catch (MessagingException e) {
            log.info("이메일 전송 실패! [회원가입]");
            throw new MailSendFailException("메일 전송이 실패하였습니다.");
        }
    }




}
