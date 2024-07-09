package com.example.cupteaapi.api;

import com.example.cupteaapi.api.model.vo.UserResponse;
import com.example.cupteaapi.service.UserService;
import com.example.db.domain.model.dto.user.UserResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저", description = "마이페이지 사용자 관련 API")
@RestController
@Slf4j
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getUser() {

        return ResponseEntity.ok()
                .body(
                        UserResponse.of(userService.getUser()
                        )
        );
    }

}
