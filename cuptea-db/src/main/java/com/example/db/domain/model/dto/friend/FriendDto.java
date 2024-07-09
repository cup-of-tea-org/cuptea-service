package com.example.db.domain.model.dto.friend;

import jakarta.persistence.Column;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class FriendDto {

    private UUID id;

    private UUID memberId;

    private String isFriend;

    private String friendLoginId;

    private Integer blockCount;

}
