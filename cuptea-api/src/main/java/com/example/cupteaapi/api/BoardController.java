package com.example.cupteaapi.api;

import com.example.cupteaapi.api.annotation.FileIsValid;
import com.example.cupteaapi.api.model.vo.CreateBoardRequest;
import com.example.cupteaapi.api.model.vo.CreateBoardResponse;
import com.example.cupteaapi.api.model.vo.UpdateBoardRequest;
import com.example.cupteaapi.api.model.vo.UpdateBoardResponse;
import com.example.cupteaapi.service.BoardService;
import com.example.db.domain.model.dto.board.CreateBoardRequestDto;
import com.example.db.domain.model.dto.board.CreateBoardResponseDto;
import com.example.db.domain.model.dto.board.UpdateBoardRequestDto;
import com.example.db.file.service.AwsS3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Board", description = "게시판 API")
@RestController
@Slf4j
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final ModelMapper modelMapper;
    private final AwsS3Service awsS3Service;
    private static final String DIR_NAME = "open";

    @Operation(summary = "게시판 글 조회")
    @GetMapping
    public ResponseEntity<?> getBoard() {
        // TODO 댓글까지 완료하고 만들기
        return null;
    }


    @Operation(summary = "게시판 글 생성")
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createBoard(
            @RequestPart(value = "file") @Valid @FileIsValid final MultipartFile file,
            @RequestPart @Valid final CreateBoardRequest createBoardRequest,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getAllErrors().get(0).getDefaultMessage());
        }

        final String uploadFileUrl = awsS3Service.upload(file, DIR_NAME);

        // VO에 url 추가
        createBoardRequest.setThumbnailUrl(uploadFileUrl);
        log.info("[BoardController] uploadFileUrl = {}", uploadFileUrl);

        CreateBoardResponseDto boardDto = boardService.createBoard(
                modelMapper.map(createBoardRequest, CreateBoardRequestDto.class)
        );

        return ResponseEntity.ok()
                .body(
                        CreateBoardResponse.of(boardDto)
                );
    }

    @Operation(summary = "게시판 글 수정")
    @PutMapping("/{update}")
    public ResponseEntity<?> updateBoard(
            @RequestPart(required = false) @Valid final MultipartFile file,
            @RequestPart @Valid final UpdateBoardRequest updateBoardRequest,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getAllErrors().get(0).getDefaultMessage());
        }

        if (!file.isEmpty()) {
            final String updateFilename = awsS3Service.upload(file, DIR_NAME);
            updateBoardRequest.setThumbnailUrl(updateFilename);
        }

        // DTO -> VO
        UpdateBoardResponse response = UpdateBoardResponse.of(
                boardService.updateBoard(
                        modelMapper.map(updateBoardRequest, UpdateBoardRequestDto.class)
                )
        );

        return ResponseEntity.ok()
                .body(response);
    }
}
