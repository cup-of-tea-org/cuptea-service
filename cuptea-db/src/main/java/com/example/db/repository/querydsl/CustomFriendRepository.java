package com.example.db.repository.querydsl;

import com.example.db.domain.model.dto.FriendDto;
import com.example.db.domain.model.entity.friend.FriendEntity;

import java.util.List;
import java.util.UUID;

public interface CustomFriendRepository {

    List<FriendDto> findAllFriendsByMemberId(final UUID memberId);

}
