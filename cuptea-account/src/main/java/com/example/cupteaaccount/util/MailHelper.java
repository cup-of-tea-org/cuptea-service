package com.example.cupteaaccount.util;

import com.example.cupteaaccount.common.model.Mail;
import com.example.db.user.repository.redis.EmailCodeRedisRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MailHelper {

    private final JavaMailSender javaMailSender;
    private final EmailCodeRedisRepository emailCodeRedisRepository;

    public void createEmail(final Mail mail) {
        setEmail(mail.getSetTo(), mail.getSubject(), mail.getText());
    }

    private void setEmail(String setTo, String subject, String text) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(setTo);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text);
            sendMail(mimeMessage);
        } catch (MessagingException e) {
            throw new MailSendException("메일 전송에 실패했습니다.");
        }
    }

    private void sendMail(MimeMessage mimeMessage) {
        javaMailSender.send(mimeMessage);
    }


}
