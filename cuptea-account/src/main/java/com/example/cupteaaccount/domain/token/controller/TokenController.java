package com.example.cupteaaccount.domain.token.controller;

import com.example.cupteaaccount.domain.token.controller.model.TokenValidationResponse;
import com.example.cupteaaccount.domain.token.service.TokenService;
import com.example.cupteaaccount.domain.token.controller.model.TokenDto;
import com.example.cupteaaccount.domain.token.controller.model.TokenValidationRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Token", description = "토큰 관련 API")
@RestController
@RequiredArgsConstructor
@Slf4j
public class TokenController {

    private final ModelMapper modelMapper;
    private final TokenService tokenService;

    @PostMapping("/api/token/validation")
    public TokenValidationResponse tokenValidation(@RequestBody final TokenValidationRequest request ) {
        log.info("token validation 시작 : {}", request );

        final TokenDto tokenDto = modelMapper.map(request, TokenDto.class);

        return modelMapper.map(
                tokenService.validateToken(tokenDto), TokenValidationResponse.class
        );
    }


}
