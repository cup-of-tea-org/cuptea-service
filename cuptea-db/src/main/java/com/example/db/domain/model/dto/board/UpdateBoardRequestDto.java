package com.example.db.domain.model.dto.board;

import lombok.*;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class UpdateBoardRequestDto {

    private UUID boardId;

    private String subject;

    private String text;

    private String thumbnailUrl;

    private Integer likeCount;

    private Integer boardCount;

    private String useYn;
}
