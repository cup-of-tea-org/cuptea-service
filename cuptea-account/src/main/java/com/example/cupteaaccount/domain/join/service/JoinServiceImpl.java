package com.example.cupteaaccount.domain.join.service;

import com.example.cupteaaccount.domain.join.controller.model.dto.EmailCodeDto;
import com.example.cupteaaccount.domain.join.controller.model.dto.EmailRequestDto;
import com.example.cupteaaccount.domain.join.controller.model.dto.JoinIdOverlappedDto;
import com.example.cupteaaccount.domain.join.exception.MailSendFailException;
import com.example.cupteaaccount.domain.join.exception.UserJoinFailException;
import com.example.cupteaaccount.domain.join.controller.model.dto.JoinUserDto;
import com.example.db.file.service.AwsS3Service;
import com.example.db.user.EmailCodeEntity;
import com.example.db.user.UserEntity;
import com.example.db.user.enums.UserRole;
import com.example.db.user.repository.EmailCodeRedisRepository;
import com.example.db.user.repository.JoinUserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JoinServiceImpl implements JoinService {

    private final AwsS3Service awsS3Service;
    private final JoinUserRepository joinUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final EmailCodeRedisRepository emailCodeRedisRepository;


    @Override
    @Transactional
    public Boolean join(final JoinUserDto joinUserDto, final MultipartFile profileImage) {

        // 기존 회원인지 검증
        if (joinUserRepository.findByLoginId(joinUserDto.getLoginId()) != null) {
            throw new UserJoinFailException("이미 가입된 회원입니다.");
        }

        // s3 저장
        final String uploadFilename = awsS3Service.upload(profileImage);

        final UserEntity user = UserEntity.builder()
                .loginId(joinUserDto.getLoginId())
                .password(passwordEncoder.encode(joinUserDto.getPassword()))
                .phone(joinUserDto.getPhone())
                .email(joinUserDto.getEmail())
                .birthday(joinUserDto.getBirthday())
                .profileImgName(uploadFilename)
                .socialType(joinUserDto.getSocialType())
                .role(UserRole.USER)
                .build();


        // MySQL 저장
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
    @Transactional
    public void sendEmail(final EmailRequestDto emailRequestDto) {
        log.info("emailRequestDto = {}", emailRequestDto.getEmail());
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        // 제목, 내용 생성
        StringBuffer subject = new StringBuffer();
        subject
                .append("[CUP TEA]\n")
                .append("이메일 인증을 위한 인증번호를 알려드립니다.\n");

        UUID randomId = UUID.randomUUID();

        StringBuffer text = new StringBuffer();
        text
                .append("인증번호는 : ")
                .append(randomId)
                .append("사이트에 돌아가셔서 인증해주세요\n")
                .append("주의 ! : 5분 이내에 입력해주셔야 합니다!\n");

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

        // 레디스에 저장
        emailCodeRedisRepository.save(EmailCodeEntity.builder()
                .id(randomId)
                .build());


    }

    @Override
    @Transactional
    public Boolean validateEmailCode(final EmailCodeDto emailCodeDto) {
        EmailCodeEntity emailCodeEntity = emailCodeRedisRepository.findById(emailCodeDto.getEmailCode())
                .orElseThrow(() -> new MailSendFailException("인증 코드가 존재하지 않습니다."));

        if (emailCodeEntity != null) {
            return true;
        }

        return false;
    }


}
