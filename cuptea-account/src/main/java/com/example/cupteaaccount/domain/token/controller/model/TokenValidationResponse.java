package com.example.cupteaaccount.domain.token.controller.model;

import lombok.*;

import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TokenValidationResponse {

    private UUID userId;
}
