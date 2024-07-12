package com.example.cupteaapi.api.model.vo;

import com.example.db.domain.model.dto.board.CreateBoardResponseDto;
import lombok.*;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class CreateBoardResponse {

    private UUID boardId;

    private UUID memberId;

    private String text;

    private String thumbnailUrl;

    private Integer likeCount;

    private Integer boardCount;

    private String useYn;

    public static CreateBoardResponse of(final CreateBoardResponseDto createBoardResponseDto) {
        return CreateBoardResponse.builder()
                        .boardId(createBoardResponseDto.getBoardId())
                        .memberId(createBoardResponseDto.getMemberId())
                        .text(createBoardResponseDto.getText())
                        .thumbnailUrl(createBoardResponseDto.getThumbnailUrl())
                        .likeCount(createBoardResponseDto.getLikeCount())
                        .boardCount(createBoardResponseDto.getBoardCount())
                        .useYn(createBoardResponseDto.getUseYn())
                        .build();
    }
}
