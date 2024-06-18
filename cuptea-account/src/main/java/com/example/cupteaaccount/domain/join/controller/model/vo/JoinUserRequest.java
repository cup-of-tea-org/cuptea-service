package com.example.cupteaaccount.domain.join.controller.model.vo;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JoinUserRequest {

    @NotBlank(message = "아이디 형식이 맞지 않습니다")
    @Length(min = 4, max = 20, message = "아이디는 4자 이상 20자 이하로 입력해주세요")
    private String loginId;

    @NotBlank(message = "password 형식이 맞지 않습니다.")
    @Pattern(
            regexp =
                    "^(?=.*[a-z])(?=.*[0-9]).{6,20}$|"
                            + "^(?=.*[a-z])(?=.*[$@$!%*#_*()?%*+=&]).{6,20}$|"
                            + "^(?=.*[A-Z])(?=.*[$@$!%*#_*()?%*+=&]).{6,20}$|"
                            + "^(?=.*[0-9])(?=.*[$@$!%*#_*()?%*+=&]).{6,20}$",
            message = "비밀번호 형식을 확인해주세요.\n" + "1. 8 ~ 20 글자\n" + "2. 영대소문자, 숫자, 특수문자 중 최소 2가지 이상 조합\n")
    private String password;

    @NotBlank(message = "전화번호 형식이 맞지 않습니다.")
    @Pattern(
            regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$",
            message = "휴대폰번호 형식을 확인해주세요.")
    @Length(max = 30, message = "휴대폰번호 의 입력길이를 확인하시기 바랍니다.")
    private String phone;

    @Email(message = "이메일 형식이 맞지 않습니다.")
    private String email;

    @Past(message = "생일은 오늘 이전 날짜로 입력해주세요")
    private LocalDateTime birthday;
}
