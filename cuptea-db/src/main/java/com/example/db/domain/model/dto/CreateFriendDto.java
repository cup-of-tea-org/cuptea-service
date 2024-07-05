package com.example.db.domain.model.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class CreateFriendDto {

    private String isFriend;
    private Integer blockCount;
    private String friendLoginId;
}
