package com.example.cupteaapi.api;

import com.example.cupteaapi.api.model.vo.CreateFriendRequest;
import com.example.cupteaapi.api.model.vo.DeleteFriendResponse;
import com.example.cupteaapi.api.model.vo.SearchFriendResponse;
import com.example.cupteaapi.api.model.vo.SearchFriendsResponse;
import com.example.cupteaapi.service.FriendService;
import com.example.db.domain.model.dto.friend.CreateFriendDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;


@Tag(name = "Friend", description = "친구 관련 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class FriendController {

    private final FriendService friendService;
    private final ModelMapper modelMapper;

    @Operation(summary = "친구 추가 API")
    @PostMapping("/friend")
    public ResponseEntity<?> createFriend(
            @RequestBody @Valid final CreateFriendRequest createFriendRequest,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getAllErrors().get(0).getDefaultMessage());
        }

        return ResponseEntity.ok()
                .body(
                        friendService.createFriend(
                                modelMapper.map(createFriendRequest, CreateFriendDto.class)
                        )
                );
    }

    @Operation(summary = "친구목록 조회 API")
    @GetMapping("/friends")
    public ResponseEntity<?> findAllFriends() {

        return ResponseEntity.ok()
                .body(
                        friendService.findAllFriends()
                                .stream().map(SearchFriendsResponse::of)
                                .collect(Collectors.toList())
                );
    }

    @Operation(summary = "친구 조회 API")
    @GetMapping("/friend/{friendId}")
    public ResponseEntity<?> findFriend(@PathVariable final String friendId) {

        if (friendId == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok()
                .body(
                        modelMapper.map(
                                friendService.findFriend(friendId), SearchFriendResponse.class)
                );
    }

    @Operation(summary = "친구 삭제 API")
    @DeleteMapping("/friend/{friendId}")
    public ResponseEntity<?> deleteFriend(@PathVariable final String friendId) {

        if (friendId == null) {
            return ResponseEntity.badRequest().body("친구 아이디가 비었습니다.");
        }

        return ResponseEntity.ok()
                .body(
                        modelMapper.map(
                                friendService.deleteFriend(friendId), DeleteFriendResponse.class
                        )
                );
    }

}
