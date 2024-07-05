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
public class SearchFriendResponse {

    private UUID id;

    private UUID memberId;

    private String isFriend;

    private String friendLoginId;

    private Integer blockCount;
}
