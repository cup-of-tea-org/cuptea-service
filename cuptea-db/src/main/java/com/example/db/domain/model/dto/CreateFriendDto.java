package com.example.db.domain.model.dto;

import lombok.*;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class CreateFriendDto {

    private UUID memberId;
    private String isFriend;
    private Integer blockCount;
    private String friendLoginId;
}
