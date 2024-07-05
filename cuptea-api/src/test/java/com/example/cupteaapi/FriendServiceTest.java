package com.example.cupteaapi;

import com.example.cupteaapi.api.model.vo.CreateFriendResponse;
import com.example.cupteaapi.service.FriendService;
import com.example.db.domain.model.dto.CreateFriendDto;
import com.example.db.domain.model.entity.user.UserEntity;
import com.example.db.domain.model.entity.user.enums.Interest;
import com.example.db.domain.model.entity.user.enums.SocialType;
import com.example.db.domain.model.entity.user.enums.UserRole;
import com.example.db.repository.FriendRepository;
import com.example.db.repository.JoinUserRepository;
import com.example.db.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class FriendServiceTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    FriendService friendService;

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
    public void createFriend() {

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


}
