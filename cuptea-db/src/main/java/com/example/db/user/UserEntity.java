package com.example.db.user;

import com.example.db.common.model.BaseEntity;
import com.example.db.user.enums.Interest;
import com.example.db.user.enums.SocialType;
import com.example.db.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Setter
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

    @Column(name = "login_id" , nullable = false)
    private String loginId;

    // 패스워드
    @Column(name = "hash_pw")
    private String password;

    @Column
    private String phone;

    @Column
    private String email;

    private LocalDate birthday;

    // 소셜로그인
    @Column(name = "social_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(nullable = false, name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Interest interest;

    @Column(name = "profile_img_name")
    private String profileImgName;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "provision")
    private Boolean provision;

}


