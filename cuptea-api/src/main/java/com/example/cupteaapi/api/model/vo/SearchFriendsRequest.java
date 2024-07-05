package com.example.cupteaapi.api.model.vo;

import com.example.db.domain.model.dto.FriendDto;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class SearchFriendsRequest {

    private List<FriendDto> friends;
}
