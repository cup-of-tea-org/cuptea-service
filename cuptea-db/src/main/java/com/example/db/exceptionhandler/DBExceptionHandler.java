package com.example.db.exceptionhandler;

import com.example.db.file.exception.FileUploadFailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class DBExceptionHandler {

    @ExceptionHandler(value = FileUploadFailException.class)
    public ResponseEntity<?> tokenNotFoundException(FileUploadFailException e) {
        log.error("", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
