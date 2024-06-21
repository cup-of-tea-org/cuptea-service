package com.example.cupteaaccount.domain.join.controller.annotation;

import com.example.cupteaaccount.util.validator.ProvisionValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = ProvisionValidator.class)
public @interface ProvisionIsValid {

    String message() default "약관에 동의하지 않았습니다.";
    Class<? extends Payload>[] payload() default {};
    Class<?>[] groups() default {};
}
