package com.example.db.domain.model.dto.board;

import lombok.*;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "boardId")
@Getter
@Setter
public class UpdateBoardResponseDto {

    private UUID boardId;

    private String subject;

    private String text;

    private String thumbnailUrl;

    private Integer likeCount;

    private Integer boardCount;

    private String useYn;
}
