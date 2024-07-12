package com.example.cupteaapi.service;

import com.example.db.domain.model.dto.board.CreateBoardRequestDto;
import com.example.db.domain.model.dto.board.CreateBoardResponseDto;
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

    private BoardEntity getBoard(final CreateBoardRequestDto createBoardRequestDto) {

        return BoardEntity.builder()
                .memberId(
                        (UUID) RequestContextHolder.getRequestAttributes()
                                .getAttribute("userId", RequestAttributes.SCOPE_REQUEST)
                )
                .text(createBoardRequestDto.getText())
                .boardCount(createBoardRequestDto.getBoardCount())
                .likeCount(createBoardRequestDto.getLikeCount())
                .boardCount(createBoardRequestDto.getBoardCount())
                .build();
    }

    private CreateBoardResponseDto createBoardResponseDto(final BoardEntity boardEntity) {
        return CreateBoardResponseDto.builder()
                .memberId(boardEntity.getMemberId())
                .text(boardEntity.getText())
                .thumbnailUrl(boardEntity.getThumbnailUrl())
                .likeCount(boardEntity.getLikeCount())
                .boardCount(boardEntity.getBoardCount())
                .useYn(boardEntity.getUseYn())
                .build();
    }


}
