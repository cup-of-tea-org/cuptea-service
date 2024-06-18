package com.example.cupteaaccount.domain.login.controller.model.vo;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class LoginRequest {

    private String loginId;
    private String password;
}
