package com.example.cupteaaccount.util.validator;

import com.example.cupteaaccount.domain.join.controller.annotation.ProvisionIsValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ProvisionValidator implements ConstraintValidator<ProvisionIsValid, Boolean> {

    @Override
    public void initialize(ProvisionIsValid constraintAnnotation) {
        log.info("provision validation start");
    }

    // 약관에 동의하지 않은 경우
    @Override
    public boolean isValid(Boolean provision, ConstraintValidatorContext constraintValidatorContext) {

        if (!provision) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("약관에 동의하지 않았습니다.")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
