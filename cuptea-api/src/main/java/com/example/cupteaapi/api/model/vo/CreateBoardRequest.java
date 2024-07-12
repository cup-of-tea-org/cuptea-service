package com.example.cupteaapi.api.model.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class CreateBoardRequest {

    @NotBlank
    private UUID memberId;

    @Size(min = 1, max = 2000)
    private String text;

    private String thumbnailUrl;

    @PositiveOrZero
    private Integer likeCount;

    @PositiveOrZero
    private Integer boardCount;

    private String useYn;

}
