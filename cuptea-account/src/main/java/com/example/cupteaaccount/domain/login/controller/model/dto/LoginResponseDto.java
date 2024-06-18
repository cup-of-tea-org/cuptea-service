package com.example.cupteaaccount.domain.login.controller.model.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class LoginResponseDto {

    String token;
}
