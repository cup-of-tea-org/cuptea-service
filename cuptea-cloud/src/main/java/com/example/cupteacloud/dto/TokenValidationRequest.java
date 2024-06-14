package com.example.cupteacloud.dto;

import lombok.*;
import org.springframework.core.ParameterizedTypeReference;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenValidationRequest extends ParameterizedTypeReference<TokenValidationRequest> {

    private TokenDto tokenDto;
}
