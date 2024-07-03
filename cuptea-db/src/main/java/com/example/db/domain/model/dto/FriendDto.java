package com.example.db.domain.model.dto;

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

    private Integer blockCount;
}
