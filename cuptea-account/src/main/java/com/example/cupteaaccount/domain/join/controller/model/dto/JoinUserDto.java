package com.example.cupteaaccount.domain.join.controller.model.dto;

import com.example.db.user.enums.Interest;
import com.example.db.user.enums.SocialType;
import com.example.db.user.enums.UserRole;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JoinUserDto {

    private String loginId;

    private String password;

    private String phone;

    private String email;

    private String interest;

    private Boolean provision;

    private LocalDate birthday;
}
