package com.example.db.domain.model.dto.friend;

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
