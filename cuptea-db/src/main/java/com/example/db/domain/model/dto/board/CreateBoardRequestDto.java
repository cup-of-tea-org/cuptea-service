package com.example.db.domain.model.dto.board;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class CreateBoardRequestDto {

    private UUID memberId;

    private String text;

    private String thumbnailUrl;

    private Integer likeCount;

    private Integer boardCount;

    private String useYn;
}
