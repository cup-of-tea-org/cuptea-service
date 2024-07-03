package com.example.db.repository.querydsl;

import com.example.db.domain.model.dto.FriendDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import static com.example.db.domain.friend.QFriendEntity.*;

@RequiredArgsConstructor
public class CustomFriendRepositoryImpl implements CustomFriendRepository {

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<FriendDto> findAllFriendsByMemberId(final UUID memberId) {
        return jpaQueryFactory
                .select(
                        Projections.constructor(
                                FriendDto.class,
                                friendEntity.id,
                                friendEntity.memberId,
                                friendEntity.isFriend,
                                friendEntity.blockCount
                        ))
                .where(
                        friendEntity.memberId.eq(memberId)
                        .and(friendEntity.isFriend.eq("Y")))
                .fetch();
    }
}
