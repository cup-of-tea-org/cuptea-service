package com.example.cupteaapi.api.model.vo;

import com.example.db.domain.model.dto.friend.FriendDto;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class SearchFriendsResponse {

    private String loginId;

    public static SearchFriendsResponse of(FriendDto friendDto) {
        return SearchFriendsResponse.builder()
                .loginId(friendDto.getMemberId().toString())
                .build();
    }
}
