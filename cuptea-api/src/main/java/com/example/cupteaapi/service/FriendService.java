package com.example.cupteaapi.service;

import com.example.cupteaapi.api.model.vo.CreateFriendResponse;
import com.example.cupteaapi.api.model.vo.SearchFriendsResponse;
import com.example.cupteaapi.exceptionhandler.exception.FriendAlreadyExistException;
import com.example.cupteaapi.exceptionhandler.exception.FriendNotFoundException;
import com.example.cupteaapi.exceptionhandler.exception.UserNotFoundException;
import com.example.db.domain.model.dto.CreateFriendDto;
import com.example.db.domain.model.dto.DeleteFriendResponseDto;
import com.example.db.domain.model.dto.FriendDto;
import com.example.db.domain.model.entity.friend.FriendEntity;
import com.example.db.repository.FriendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendService {

    private final FriendRepository friendRepository;

    /**
     * 친구 추가
     */

    @Transactional(rollbackFor = IllegalAccessException.class)
    public CreateFriendResponse createFriend(final CreateFriendDto createFriendDto) {
        FriendEntity friend = getFriend(createFriendDto);

        FriendEntity saveFriend = friendRepository.save(friend);

        return CreateFriendResponse.builder()
                .friendLoginId(saveFriend.getFriendLoginId())
                .build();
    }

    /**
     * 해당 멤버의 모든 친구 조회
     */
    @Transactional(readOnly = true)
    public List<FriendDto> findAllFriends() {

        var userId = Objects.requireNonNull(RequestContextHolder.getRequestAttributes()).getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
        // null check
        if (userId == null) {
            throw new UserNotFoundException("RequestContextHolder userId 없음");
        }

        log.info("[FriendService] RequestContextHolder userId = {}", userId);

        return friendRepository.findAllFriendsByMemberId(UUID.fromString(userId.toString()));
    }

    @Transactional(rollbackFor = IllegalAccessException.class)
    public DeleteFriendResponseDto deleteFriend(final String friendLoginId) {

        // 현재 사용자
        final UUID userId = (UUID) RequestContextHolder.getRequestAttributes()
                .getAttribute("userId", RequestAttributes.SCOPE_REQUEST);

        FriendEntity findFriend = friendRepository.findByFriendLoginIdAndMemberId(friendLoginId, userId)
                .orElseThrow(() -> new FriendNotFoundException("친구를 찾을 수 없습니다."));
        // 상태 N으로 변경
        findFriend.setIsFriend("N");

        return DeleteFriendResponseDto.builder()
                .friendLoginId(findFriend.getFriendLoginId())
                .build();
    }

    // 친구 엔티티 생성
    private FriendEntity getFriend(final CreateFriendDto createFriendDto) {
        // 현재 사용자
        UUID memberId = (UUID) RequestContextHolder
                .getRequestAttributes().getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
        // 친구 아이디
        String friendLoginId = createFriendDto.getFriendLoginId();

        // 이미 친구인지 확인
        isAlreadyFriend(memberId, friendLoginId);

        return FriendEntity.builder()
                .memberId(memberId)
                .friendLoginId(friendLoginId)
                .blockCount(createFriendDto.getBlockCount())
                .build();
    }

    // 친구가 이미 있으면 생성 x 없으면 친구 생성
    private void isAlreadyFriend(final UUID memberId, final String friendLoginId) {
        FriendEntity findFriend = friendRepository.findByMemberIdAndFriendLoginId(memberId, friendLoginId);

        if (findFriend != null) {
            throw new FriendAlreadyExistException("이미 친구목록에 등록된 친구 입니다.");
        }
    }

}
