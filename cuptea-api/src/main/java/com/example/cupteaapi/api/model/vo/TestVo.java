package com.example.cupteaapi.api.model.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TestVo {

    @NotBlank(message = "이름은 필수값입니다")
    private String name;

    @Min(value = 10)
    private int age;

    @Email(message = "이메일 형식이 아닙니다")
    private String email;
}
