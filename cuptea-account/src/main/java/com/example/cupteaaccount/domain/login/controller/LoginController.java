package com.example.cupteaaccount.domain.login.controller;


import com.example.cupteaaccount.domain.login.controller.model.dto.FindIdRequestDto;
import com.example.cupteaaccount.domain.login.controller.model.dto.FindIdResponseDto;
import com.example.cupteaaccount.domain.login.controller.model.dto.FindPasswordRequestDto;
import com.example.cupteaaccount.domain.login.controller.model.vo.FindIdRequest;
import com.example.cupteaaccount.domain.login.controller.model.vo.FindIdResponse;
import com.example.cupteaaccount.domain.login.controller.model.vo.FindPasswordRequest;
import com.example.cupteaaccount.domain.login.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

/**
 * 로그인은 시큐리티 필터 쪽에서 진행 이름만 LoginController 라고 명시한 클래스
 * (아이디, 비밀번호 찾기)
 */
@RestController
@RequestMapping("/open-api/user")
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final ModelMapper modelMapper;
    private final LoginService loginService;

    // 아이디 찾기
    @PostMapping("/id")
    public ResponseEntity<?> findUserByEmail(
            @RequestBody @Valid final FindIdRequest findIdRequest,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getAllErrors().get(0).getDefaultMessage());
        }

        FindIdResponseDto findIdResponseDto = loginService.findUserByEmail(modelMapper.map(findIdRequest, FindIdRequestDto.class));

        return ResponseEntity.ok()
                .body(modelMapper.map(findIdResponseDto, FindIdResponse.class));
    }

    @PostMapping("/password")
    public ResponseEntity<?> findUserPassword(
            @RequestBody @Valid final FindPasswordRequest request,
            Errors errors
    ) {
        log.info("findUserPassword request ID : {}", request.getLoginId());
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getAllErrors().get(0).getDefaultMessage());
        }

        loginService.findPassword(modelMapper.map(request, FindPasswordRequestDto.class));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/password/{emailCode}")
    public ResponseEntity<?> validatePasswordCode(
            @PathVariable("emailCode") String emailCode
    ) {
        log.info("validatePasswordCode emailCode : {}", emailCode);
        if (!StringUtils.hasText(emailCode)) {
            return ResponseEntity.badRequest().body("인증코드를 입력해주세요.");
        }
        loginService.validateEmailCode(emailCode);
        return ResponseEntity.ok().build();
    }

    //TODO PUTMAPPING 비밀번호 수정 API

}
