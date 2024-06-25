package com.example.cupteaaccount.domain.login.controller.model.vo;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class LoginResponse {

    private String token;
}
