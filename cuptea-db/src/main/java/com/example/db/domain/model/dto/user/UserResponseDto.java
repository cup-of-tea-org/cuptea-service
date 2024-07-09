package com.example.db.domain.model.dto.user;

import com.example.db.domain.model.entity.user.UserEntity;
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
public class UserResponseDto {

    private String loginId;
    private String email;
    private String phone;
    private Interest interest;
    private LocalDate birthday;


}
