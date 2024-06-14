package com.example.cupteacloud.dto;

import lombok.*;

import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "userId")
public class TokenValidationResponse {

    private UUID userId;
}
