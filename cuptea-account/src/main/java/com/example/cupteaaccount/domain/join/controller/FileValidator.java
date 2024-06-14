package com.example.cupteaaccount.domain.join.controller;

import com.example.cupteaaccount.domain.join.controller.annotation.FileIsValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
public class FileValidator implements ConstraintValidator<FileIsValid, MultipartFile> {

    private static final String FILE_TYPE = "image/png";

    @Override
    public void initialize(FileIsValid constraintAnnotation) {
        log.info("file validation start");
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        log.info("Validating~~~~~)");
        log.info(multipartFile.getContentType());

        if (multipartFile.isEmpty()) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("파일이 비어있습니다.").addConstraintViolation();
            return false;
        }

        if (!StringUtils.hasText(multipartFile.getOriginalFilename())) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("파일 이름이 없습니다.").addConstraintViolation();
            return false;
        }

        if (!FILE_TYPE.equals(multipartFile.getContentType())) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("파일 형식이 맞지 않습니다.").addConstraintViolation();
            return false;
        }

        return true;
    }
}
