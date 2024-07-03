package com.example.cupteaaccount.domain.join.controller.model.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JoinUserDto {

    private String loginId;

    private String password;

    private String phone;

    private String email;

    private String interest;

    private Boolean provision;

    private LocalDate birthday;
}
