package com.example.cupteaaccount.domain.join.service;

import com.example.cupteaaccount.common.model.Mail;
import com.example.cupteaaccount.domain.join.controller.model.dto.EmailCodeDto;
import com.example.cupteaaccount.domain.join.controller.model.dto.EmailRequestDto;
import com.example.cupteaaccount.domain.join.controller.model.dto.JoinIdOverlappedDto;
import com.example.cupteaaccount.domain.join.exception.MailSendFailException;
import com.example.cupteaaccount.domain.join.exception.UserJoinFailException;
import com.example.cupteaaccount.domain.join.controller.model.dto.JoinUserDto;
import com.example.cupteaaccount.util.MailHelper;
import com.example.db.file.service.AwsS3Service;
import com.example.db.user.repository.redis.EmailCodeEntity;
import com.example.db.user.UserEntity;
import com.example.db.user.enums.Interest;
import com.example.db.user.enums.SocialType;
import com.example.db.user.enums.UserRole;
import com.example.db.user.repository.redis.EmailCodeRedisRepository;
import com.example.db.user.repository.JoinUserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
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
    private final MailHelper mailHelper;

    private static final String DIR_NAME = "open";

    @Override
    @Transactional
    public Boolean join(final JoinUserDto joinUserDto, final MultipartFile profileImage) {


        // 기존 회원인지 검증
        if (joinUserRepository.findByLoginId(joinUserDto.getLoginId()) != null) {
            throw new UserJoinFailException("이미 가입된 회원입니다.");
        }

        // s3 저장
        // storage name : open/ [파일이름]
        final String uploadFilename = awsS3Service.upload(profileImage, DIR_NAME);

        // 관심사 convert
        Interest convertInterest = convertInterest(joinUserDto.getInterest());
        // create UserEntity
        final UserEntity user = getUserEntity(joinUserDto, uploadFilename, convertInterest);

        // MySQL 저장
        joinUserRepository.save(user);

        return true;
    }

    private UserEntity getUserEntity(JoinUserDto joinUserDto, String uploadFilename, Interest convertInterest) {
        final UserEntity user = UserEntity.builder()
                .loginId(joinUserDto.getLoginId())
                .password(passwordEncoder.encode(joinUserDto.getPassword()))
                .phone(joinUserDto.getPhone())
                .email(joinUserDto.getEmail())
                .birthday(joinUserDto.getBirthday())
                .profileImgName(uploadFilename)
                .socialType(SocialType.NONE)
                .interest(convertInterest)
                .role(UserRole.USER)
                .build();
        return user;
    }


    @Override
    @Transactional(readOnly = true)
    public Boolean isIdOverlapped(final JoinIdOverlappedDto joinIdOverlappedDto) {
        log.info("joinIdOverlappedDto = {}", joinIdOverlappedDto.getLoginId());
        UserEntity findUser = joinUserRepository.findByLoginId(joinIdOverlappedDto.getLoginId());
        if (findUser == null) {
            return true; // id 중복 x
        }

        return false; // id 중복 o
    }

    // 이메일 생성
    @Override
    @Transactional(rollbackFor = MessagingException.class)
    public void sendEmail(final EmailRequestDto emailRequestDto) {
        log.info("emailRequestDto = {}", emailRequestDto.getEmail());

        UserEntity findUser = joinUserRepository.findByEmail(emailRequestDto.getEmail());

        if (findUser != null) {
            throw new UserJoinFailException("이미 가입된 이메일입니다.");
        }

        // 제목, 내용 생성
        StringBuffer subject = new StringBuffer();
        subject
                .append("[CUP TEA]\n")
                .append("이메일 인증을 위한 인증번호를 알려드립니다.\n");

        UUID randomId = UUID.randomUUID();

        StringBuffer text = new StringBuffer();
        text
                .append("인증번호는 : ")
                .append(randomId + "\n")
                .append("사이트에 돌아가셔서 인증해주세요\n")
                .append("주의 ! : 5분 이내에 입력해주셔야 합니다!\n");

        //  create mail && 이메일 전송
            mailHelper.createEmail(
                    Mail.builder()
                            .setTo(emailRequestDto.getEmail())
                            .subject(subject.toString())
                            .text(text.toString())
                            .build()
            );
            log.info("이메일 전송 완료! [회원가입]");
        // 레디스에 저장
        emailCodeRedisRepository.save(EmailCodeEntity.builder()
                .id(randomId)
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean validateEmailCode(final EmailCodeDto emailCodeDto) {
        emailCodeRedisRepository
                .findById(UUID.fromString(emailCodeDto.getEmailCode()))
                .orElseThrow(() -> new MailSendFailException("인증 코드가 존재하지 않습니다."));

        log.info("이메일 코드 인증 성공!!");
        return true;
    }

    // interest enum 변환
    private Interest convertInterest(String interest) {
        return Interest.valueOf(interest.toUpperCase());
    }


}
