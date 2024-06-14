package com.example.cupteaaccount.domain.token.controller.model;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class TokenValidationRequest {

    private String token;
}
