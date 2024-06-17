package com.example.cupteaaccount.domain.join.service;

import com.example.cupteaaccount.domain.join.controller.model.dto.EmailCodeDto;
import com.example.cupteaaccount.domain.join.controller.model.dto.EmailRequestDto;
import com.example.cupteaaccount.domain.join.controller.model.dto.JoinIdOverlappedDto;
import com.example.cupteaaccount.domain.join.controller.model.dto.JoinUserDto;
import org.springframework.web.multipart.MultipartFile;

public interface JoinService {

    Boolean join(JoinUserDto joinUserDto, MultipartFile profileImage);

    Boolean isIdOverlapped(JoinIdOverlappedDto joinIdOverlappedDto);

    void sendEmail(EmailRequestDto emailRequestDto);

    Boolean validateEmailCode(EmailCodeDto emailCodeDto);
}
