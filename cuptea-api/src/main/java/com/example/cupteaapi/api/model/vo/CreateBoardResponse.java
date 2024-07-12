package com.example.cupteaapi.api.model.vo;

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

    private UUID memberId;

    private String text;

    private String thumbnailUrl;

    private Integer likeCount;

    private Integer boardCount;

    private String useYn;
}
