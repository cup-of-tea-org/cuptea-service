package com.example.cupteaapi.exceptionhandler;

import com.example.cupteaapi.domain.user.exception.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {
        // token 검증 실패 401
        @ExceptionHandler(value = UserNotFoundException.class)
        public ResponseEntity<?> tokenNotFoundException(UserNotFoundException e) {
            log.error("", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }


}


