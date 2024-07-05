package com.example.cupteaapi.exceptionhandler;

import com.example.cupteaapi.exceptionhandler.exception.FriendAlreadyExistException;
import com.example.cupteaapi.exceptionhandler.exception.FriendNotFoundException;
import com.example.cupteaapi.exceptionhandler.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
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

        // 친구가 이미 존재할 시
        @ExceptionHandler(value = FriendAlreadyExistException.class)
        public ResponseEntity<?> friendAlreadyExistException(FriendAlreadyExistException e) {
            log.error("", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    // 찾을 수 없는 친구
    @ExceptionHandler(value = FriendNotFoundException.class)
    public ResponseEntity<?> friendNotFoundException(FriendNotFoundException e) {
        log.error("", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }




}


