package com.example.cupteaapi.service;

import com.example.db.domain.model.dto.board.CreateBoardRequestDto;
import com.example.db.domain.model.dto.board.CreateBoardResponseDto;
import com.example.db.domain.model.dto.board.UpdateBoardRequestDto;
import com.example.db.domain.model.dto.board.UpdateBoardResponseDto;
import com.example.db.domain.model.entity.board.BoardEntity;
import com.example.db.file.service.AwsS3Service;
import com.example.db.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final AwsS3Service awsS3Service;


    @Transactional
    public CreateBoardResponseDto createBoard(final CreateBoardRequestDto createBoardRequestDto) {

        //
        BoardEntity savedBoard = boardRepository.save(
                getBoard(createBoardRequestDto)
        );

        return createBoardResponseDto(savedBoard);

    }

    @Transactional
    public UpdateBoardResponseDto updateBoard(final UpdateBoardRequestDto updateBoardRequestDto) {
        BoardEntity findBoardEntity = boardRepository.findById(updateBoardRequestDto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        UpdateBoardResponseDto updateBoardResponseDto = updateBoard(updateBoardRequestDto, findBoardEntity);

        log.info("[BoardService] updateBoardResponseDto = {}", updateBoardResponseDto);

        return updateBoardResponseDto;
    }

    private BoardEntity getBoard(final CreateBoardRequestDto createBoardRequestDto) {

        return BoardEntity.builder()
                .memberId(
                        (UUID) RequestContextHolder.getRequestAttributes()
                                .getAttribute("userId", RequestAttributes.SCOPE_REQUEST)
                )
                .subject(createBoardRequestDto.getSubject())
                .text(createBoardRequestDto.getText())
                .boardCount(createBoardRequestDto.getBoardCount())
                .likeCount(createBoardRequestDto.getLikeCount())
                .boardCount(createBoardRequestDto.getBoardCount())
                .build();
    }

    private CreateBoardResponseDto createBoardResponseDto(final BoardEntity boardEntity) {
        return CreateBoardResponseDto.builder()
                .boardId(boardEntity.getBoardId())
                .memberId(boardEntity.getMemberId())
                .subject(boardEntity.getSubject())
                .text(boardEntity.getText())
                .thumbnailUrl(boardEntity.getThumbnailUrl())
                .likeCount(boardEntity.getLikeCount())
                .boardCount(boardEntity.getBoardCount())
                .useYn(boardEntity.getUseYn())
                .build();
    }

    private UpdateBoardResponseDto updateBoard(
            final UpdateBoardRequestDto updateBoardRequestDto,
            final BoardEntity findBoardEntity
    ) {
        if (updateBoardRequestDto.getSubject() != null) {
            findBoardEntity.setSubject(updateBoardRequestDto.getSubject());
        }

        if (updateBoardRequestDto.getText() != null) {
            findBoardEntity.setText(updateBoardRequestDto.getText());
        }

        if (updateBoardRequestDto.getThumbnailUrl() != null) {
            findBoardEntity.setThumbnailUrl(updateBoardRequestDto.getThumbnailUrl());
        }

        // 좋아요 개수를 수정할 시 좋아요 개수를 1 증가시킨다.
        if (updateBoardRequestDto.getLikeCount() != null) {
            findBoardEntity.setLikeCount(updateBoardRequestDto.getLikeCount() + 1);
        }

        // 게시글 조회 수를 수정할 시 게시글 개수를 1 증가시킨다.
        if (updateBoardRequestDto.getBoardCount() != null) {
            findBoardEntity.setBoardCount(updateBoardRequestDto.getBoardCount() + 1);
        }

        if (updateBoardRequestDto.getUseYn() != null) {
            findBoardEntity.setUseYn(updateBoardRequestDto.getUseYn());
        }

        // Entity -> Dto
        return UpdateBoardResponseDto.builder()
                .boardId(findBoardEntity.getBoardId())
                .subject(findBoardEntity.getSubject())
                .text(findBoardEntity.getText())
                .thumbnailUrl(findBoardEntity.getThumbnailUrl())
                .useYn(findBoardEntity.getUseYn())
                .likeCount(findBoardEntity.getLikeCount())
                .boardCount(findBoardEntity.getBoardCount())
                .build();
    }

}
