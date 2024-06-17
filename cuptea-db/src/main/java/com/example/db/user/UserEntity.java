package com.example.db.user;

import com.example.db.common.model.BaseEntity;
import com.example.db.user.enums.SocialType;
import com.example.db.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@EqualsAndHashCode(of = "id", callSuper = true)
@ToString(callSuper = true)
@Table(name = "user")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "login_id")
    private String loginId;

    // 패스워드
    @Column(name = "hash_pw")
    private String password;

    private String phone;

    @Column(nullable = false)
    private String email;

    private LocalDateTime birthday;

    // 소셜로그인
    @Column(name = "social_type")
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(nullable = false, name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "profile_img_name")
    private String profileImgName;

}


