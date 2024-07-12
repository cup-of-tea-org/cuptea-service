package com.example.cupteaapi.util;

import com.example.cupteaapi.api.annotation.FileIsValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class FileValidator implements ConstraintValidator<FileIsValid, MultipartFile> {
    @Override
    public void initialize(FileIsValid constraintAnnotation) {
        log.info("[FileValidator] file validation start");
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        log.info("[FileValidator] Validating~~~~~)");

        if (multipartFile.isEmpty()) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("파일이 비어있습니다.").addConstraintViolation();
            return false;
        }

        if (!multipartFile.getOriginalFilename().contains(".png")) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("파일 형식이 맞지 않습니다.").addConstraintViolation();
            return false;
        }

        return true;
    }
}
