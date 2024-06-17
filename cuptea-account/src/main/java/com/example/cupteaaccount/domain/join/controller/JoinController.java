package com.example.cupteaaccount.domain.join.controller;

import com.example.cupteaaccount.domain.join.controller.annotation.FileIsValid;
import com.example.cupteaaccount.domain.join.controller.model.dto.EmailCodeDto;
import com.example.cupteaaccount.domain.join.controller.model.dto.EmailRequestDto;
import com.example.cupteaaccount.domain.join.controller.model.dto.JoinIdOverlappedDto;
import com.example.cupteaaccount.domain.join.controller.model.vo.*;
import com.example.cupteaaccount.domain.join.controller.model.dto.JoinUserDto;
import com.example.cupteaaccount.domain.join.service.JoinService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/open-api/join")
@Slf4j
public class JoinController {

    private final JoinService joinService;
    private final ModelMapper modelMapper;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ApiResponse(description = "회원가입 API")
    public ResponseEntity<?> join(
            @RequestPart @Valid final JoinUserRequest joinUserRequest,
            @RequestPart(required = false, value = "profileImage") @Valid @FileIsValid final MultipartFile profileImage,
            Errors errors
    ) {
        log.info("multipartFile = {}", profileImage.getOriginalFilename());
        // validation
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getAllErrors().get(0).getDefaultMessage());
        }
        // VO -> DTO
        Boolean result = joinService.join(modelMapper.map(joinUserRequest, JoinUserDto.class), profileImage);

        if(result) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


    @PostMapping("/id-check")
    @ApiResponse(description = "아이디 중복 체크 API")
    public ResponseEntity<?> idOverlapped(
            @RequestBody @Valid final JoinIdOverlappedRequest request,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getAllErrors().get(0).getDefaultMessage());
        }

        // VO -> DTO
        Boolean result = joinService.isIdOverlapped(modelMapper.map(request, JoinIdOverlappedDto.class));

        return result ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 사용중인 아이디입니다.");
    }

    @PostMapping("/validate-email")
    @ApiResponse(description = "이메일 인증 API")
    public ResponseEntity<?> emailValidate(
            @RequestBody @Valid final EmailRequest emailRequest,
            Errors errors
    ) {
        log.info("emailRequest = {}", emailRequest.getEmail());
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getAllErrors().get(0).getDefaultMessage());
        }

        // VO -> DTO
        joinService.sendEmail(modelMapper.map(emailRequest, EmailRequestDto.class));

        return ResponseEntity.ok().build();
    }

    // TODO 이메일 코드 검증 API 생성
    @PostMapping("/validate-email-code")
    @ApiResponse(description = "이메일 코드 검증 API")
    public ResponseEntity<?> emailCodeValidate(
            @RequestBody @Valid final EmailCodeRequest emailCodeRequest,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getAllErrors().get(0).getDefaultMessage());
        }

        final Boolean result = joinService.validateEmailCode(modelMapper.map(emailCodeRequest, EmailCodeDto.class));

        if (result) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증번호가 일치하지 않습니다.");
        }
    }


}
