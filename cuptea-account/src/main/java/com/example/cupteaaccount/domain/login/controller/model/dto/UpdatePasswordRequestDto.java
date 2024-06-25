package com.example.cupteaaccount.domain.login.controller.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class UpdatePasswordRequestDto {

    private String loginId;
    private String password;
}
