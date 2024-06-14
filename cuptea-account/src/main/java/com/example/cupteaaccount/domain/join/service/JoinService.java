package com.example.cupteaaccount.domain.join.service;

import com.example.cupteaaccount.domain.join.controller.model.dto.EmailRequestDto;
import com.example.cupteaaccount.domain.join.controller.model.dto.JoinIdOverlappedDto;
import com.example.cupteaaccount.domain.join.controller.model.dto.JoinUserDto;

public interface JoinService {

    Boolean join(JoinUserDto joinUserDto);

    Boolean isIdOverlapped(JoinIdOverlappedDto joinIdOverlappedDto);

    void sendEmail(EmailRequestDto emailRequestDto);
}
