package com.example.cupteaaccount.domain.login.controller.model.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class FindPasswordRequest {
    @NotBlank(message = "아이디를 입력해주세요.")
    private String loginId;
    @Email(message = "이메일 형식이 맞지 않습니다.")
    private String email;
}
