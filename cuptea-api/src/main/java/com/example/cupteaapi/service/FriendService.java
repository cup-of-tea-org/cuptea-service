package com.example.cupteaapi.service;

import com.example.cupteaapi.exceptionhandler.exception.UserNotFoundException;
import com.example.db.domain.model.dto.FriendDto;
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
     * 해당 멤버의 모든 친구 조회
     */
    @Transactional(readOnly = true)
    public List<FriendDto> findAllFriends() {

        var userId = Objects.requireNonNull(RequestContextHolder.getRequestAttributes()).getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
        // null check
        if (userId == null) {
            throw new UserNotFoundException("RequestContextHolder userId 없음");
        }

        log.info("[FriendService] RequestContextHolder userId = {}", userId.toString());

        return friendRepository.findAllFriendsByMemberId(UUID.fromString(userId.toString()));
    }
}
