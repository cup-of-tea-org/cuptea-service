package com.example.cupteaapi.api;

import com.example.cupteaapi.api.model.FriendsSearchResponse;
import com.example.cupteaapi.service.FriendService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Friend", description = "친구 관련 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class FriendController {

    private final FriendService friendService;

    @GetMapping("/friends")
    public ResponseEntity<?> findAllFriends() {

        return ResponseEntity.ok()
                .body(FriendsSearchResponse.builder()
                        .friends(friendService.findAllFriends())
                        .build());
    }
}
