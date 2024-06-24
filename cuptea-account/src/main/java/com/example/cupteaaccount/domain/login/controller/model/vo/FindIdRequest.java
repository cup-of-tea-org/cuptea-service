package com.example.cupteaaccount.domain.login.controller.model.vo;

import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class FindIdRequest {

    @Email(message = "이메일 형식 맞지 않습니다.")
    private String email;
}
