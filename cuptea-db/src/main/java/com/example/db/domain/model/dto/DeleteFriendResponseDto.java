package com.example.db.domain.model.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class DeleteFriendResponseDto {

    private String friendLoginId;
}
