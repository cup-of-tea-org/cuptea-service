package com.example.cupteaapi;

import com.example.cupteaapi.api.model.vo.CreateFriendResponse;
import com.example.cupteaapi.service.FriendService;
import com.example.db.domain.model.dto.friend.CreateFriendDto;
import com.example.db.domain.model.entity.friend.FriendEntity;
import com.example.db.domain.model.entity.user.UserEntity;
import com.example.db.domain.model.entity.user.enums.Interest;
import com.example.db.domain.model.entity.user.enums.SocialType;
import com.example.db.domain.model.entity.user.enums.UserRole;
import com.example.db.repository.FriendRepository;
import com.example.db.repository.JoinUserRepository;
import com.example.db.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class FriendServiceTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    FriendService friendService;

    @Autowired
    FriendRepository friendRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JoinUserRepository joinUserRepository;

    @BeforeEach
    public void setUp() {
        joinUserRepository.saveAll(
                List.of(
                        UserEntity.builder()
                                .loginId("test1")
                                .birthday(LocalDate.now().minusDays(1))
                                .password("test")
                                .email("ws5501@naver.com")
                                .phone("01012345678")
                                .socialType(SocialType.NONE)
                                .role(UserRole.USER)
                                .provision(true)
                                .interest(Interest.NONE).build(),

                        UserEntity.builder()
                                .loginId("test2")
                                .birthday(LocalDate.now().minusDays(1))
                                .password("test")
                                .email("ws5501@naver.com")
                                .phone("01012345678")
                                .socialType(SocialType.NONE)
                                .role(UserRole.USER)
                                .provision(true)
                                .interest(Interest.NONE).build(),

                        UserEntity.builder()
                                .loginId("test3")
                                .birthday(LocalDate.now().minusDays(1))
                                .password("test")
                                .email("ws5501@naver.com")
                                .role(UserRole.USER)
                                .socialType(SocialType.NONE)
                                .phone("01012345678")
                                .provision(true)
                                .interest(Interest.NONE).build()
                )

        );
    }

    @AfterEach
    public void down() {
        entityManager.clear();
        entityManager.close();
    }

    @Test
    @DisplayName("친구 추가")
    void createFriend() {

        // Given
        final UserEntity findUser = userRepository.findByLoginId("test1");

        // When
        final CreateFriendResponse createFriend = friendService.createFriend(CreateFriendDto.builder()
                .memberId(findUser.getId())
                .isFriend("Y")
                .friendLoginId("test2")
                .build()
        );

        // Then
        assertThat(createFriend.getFriendLoginId()).isEqualTo("test2");
    }

    @Test
    @DisplayName("친구 조회")
    void findFriend() throws Exception {
        // Given

        final UserEntity findUser = userRepository.findByLoginId("test1");

        final CreateFriendResponse createFriend = friendService.createFriend(CreateFriendDto.builder()
                .memberId(findUser.getId())
                .isFriend("Y")
                .friendLoginId("test2")
                .build()
        );
        // When
        final FriendEntity findFriend = friendRepository.findByMemberId(findUser.getId());
        // Then

        assertThat(friendRepository.findByMemberIdAndFriendLoginId(findUser.getId(), "test2")).isEqualTo(findFriend);
    }

    @Test
    @DisplayName("친구 전체 조회")
    void fidnFriends() throws Exception {
        // Given
        UUID memberId = userRepository.findByLoginId("test1").getId();
        friendRepository.saveAll(
                List.of(
                        FriendEntity.builder()
                                .memberId(memberId)
                                .isFriend("Y")
                                .friendLoginId("test2")
                                .build(),
                        FriendEntity.builder()
                                .memberId(memberId)
                                .isFriend("Y")
                                .friendLoginId("test3")
                                .build()
                )
        );
        // When
        // Then
        assertThat(friendRepository.findAllFriendsByMemberId(memberId).size()).isEqualTo(2);
    }


}
