package com.example.cupteaapi.api.model.vo;

import com.example.db.domain.model.dto.user.UserResponseDto;
import com.example.db.domain.model.entity.user.enums.Interest;
import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class UserResponse {

    private String loginId;
    private String email;
    private String phone;
    private Interest interest;
    private LocalDate birthday;

    public static UserResponse of(UserResponseDto userResponseDto) {
        return UserResponse.builder()
                .loginId(userResponseDto.getLoginId())
                .email(userResponseDto.getEmail())
                .phone(userResponseDto.getPhone())
                .interest(userResponseDto.getInterest())
                .birthday(userResponseDto.getBirthday())
                .build();
    }
}
