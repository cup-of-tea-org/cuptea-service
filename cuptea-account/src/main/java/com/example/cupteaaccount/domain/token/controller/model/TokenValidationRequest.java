package com.example.cupteaaccount.domain.token.controller.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class TokenValidationRequest {

    private String token;
}
