package com.example.cupteaaccount.domain.join.controller.model.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JoinIdOverlappedRequest {

    @NotBlank(message = "아이디 형식이 맞지 않습니다")
    @Pattern(regexp = "^[a-zA-Z]{4,20}$", message = "아이디는 영문자로 4자 이상 20자 이하로 입력해주세요")
    private String loginId;
}
