package com.example.cupteaaccount.domain.join.controller.model.dto;

import com.example.cupteainfrastructure.user.enums.SocialType;
import com.example.cupteainfrastructure.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

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

    private LocalDateTime birthday;

    private SocialType socialType;

    private UserRole role;

    private String profileImgName;
}
