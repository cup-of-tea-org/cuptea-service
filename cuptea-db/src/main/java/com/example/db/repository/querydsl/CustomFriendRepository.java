package com.example.db.repository.querydsl;

import com.example.db.domain.model.dto.friend.FriendDto;

import java.util.List;
import java.util.UUID;

public interface CustomFriendRepository {

    List<FriendDto> findAllFriendsByMemberId(final UUID memberId);

}
