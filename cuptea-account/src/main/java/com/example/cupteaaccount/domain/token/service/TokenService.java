package com.example.cupteaaccount.domain.token.service;

import com.example.cupteaaccount.domain.token.controller.model.TokenDto;

import java.util.UUID;

public interface TokenService {


    UUID validateToken(TokenDto token);
}
