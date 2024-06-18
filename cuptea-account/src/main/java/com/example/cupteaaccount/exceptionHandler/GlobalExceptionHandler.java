package com.example.cupteaaccount.exceptionHandler;

import com.example.cupteaaccount.domain.join.exception.MailSendFailException;
import com.example.cupteaaccount.domain.login.exception.UserNotFoundException;
import com.example.cupteaaccount.domain.token.exception.TokenNotFoundException;
import com.example.cupteaaccount.domain.join.exception.UserJoinFailException;
import io.jsonwebtoken.JwtException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@Order(value = Integer.MIN_VALUE)
public class GlobalExceptionHandler {

    // token 검증 실패 401
    @ExceptionHandler(value = TokenNotFoundException.class)
    public ResponseEntity<?> tokenNotFoundException(TokenNotFoundException e) {
        log.error("", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // 회원가입 실패 exception
    @ExceptionHandler(value = UserJoinFailException.class)
    public ResponseEntity<?> userJoinFailException(UserJoinFailException e) {
        log.error("", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 유저 검증 실패 (security exception) 401
    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<?> authenticationException(AuthenticationException e) {
        log.error("", e);
        return new ResponseEntity<>("유저 인증 실패", HttpStatus.UNAUTHORIZED);
    }

    // 유저 찾기 실패
    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<?> userNotFoundException(UserNotFoundException e) {
        log.error("", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    //  메일 전송 실패
    @ExceptionHandler(value = MailSendFailException.class)
    public ResponseEntity<?> mailSendFailException(MailSendFailException e) {
        log.error("", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    // custom file validation
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException constraintViolationException) {

        constraintViolationException.getConstraintViolations().stream().peek(o -> log.error(o.getMessage()));

        return new ResponseEntity<>("파일 입력 에러입니다.", HttpStatus.BAD_REQUEST);
    }

    // jwt exception
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<?> handleConstraintViolationException(JwtException e) {

        log.error("", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
