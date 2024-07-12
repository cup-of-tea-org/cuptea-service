package com.example.cupteaapi.api.model.vo;


import com.example.db.domain.model.dto.board.UpdateBoardResponseDto;
import lombok.*;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class UpdateBoardResponse {

    private UUID boardId;

    private String subject;

    private String text;

    private String thumbnailUrl;

    private Integer likeCount;

    private Integer boardCount;

    private String useYn;

    public static UpdateBoardResponse of(final UpdateBoardResponseDto updateBoardResponseDto) {
        return UpdateBoardResponse.builder()
                .boardId(updateBoardResponseDto.getBoardId())
                .subject(updateBoardResponseDto.getSubject())
                .text(updateBoardResponseDto.getText())
                .thumbnailUrl(updateBoardResponseDto.getThumbnailUrl())
                .likeCount(updateBoardResponseDto.getLikeCount())
                .boardCount(updateBoardResponseDto.getBoardCount())
                .useYn(updateBoardResponseDto.getUseYn())
                .build();
    }
}
