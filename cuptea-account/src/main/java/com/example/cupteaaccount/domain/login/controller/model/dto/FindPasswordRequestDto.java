package com.example.cupteaaccount.domain.login.controller.model.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class FindPasswordRequestDto {

    private String loginId;
    private String email;
}
