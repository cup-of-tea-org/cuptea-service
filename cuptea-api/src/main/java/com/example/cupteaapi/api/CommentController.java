package com.example.cupteaapi.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Comment", description = "댓글 API")
@RestController
@Slf4j
@RequestMapping("/api/comment")
public class CommentController {

    @GetMapping
    public ResponseEntity<?> getComment() {
        // TODO 댓글 조회
        return null;
    }

    @PostMapping
    public ResponseEntity<?> createComment() {
        // TODO 댓글 생성
        return null;
    }

    @PutMapping
    public ResponseEntity<?> updateComment() {
        // TODO 댓글 수정
        return null;
    }

    @DeleteMapping
    public ResponseEntity<?> deleteComment() {
        // TODO 댓글 삭제
        return null;
    }
}
