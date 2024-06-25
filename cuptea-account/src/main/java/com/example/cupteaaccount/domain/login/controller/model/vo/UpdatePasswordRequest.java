package com.example.cupteaaccount.domain.login.controller.model.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class UpdatePasswordRequest {

    @NotBlank(message = "아이디 형식이 맞지 않습니다")
    @Pattern(regexp = "^[a-zA-Z]{4,20}$", message = "아이디는 영문자로 4자 이상 20자 이하로 입력해주세요")
    private String loginId;

    @Pattern(
            regexp =
                    "^(?=.*[a-z])(?=.*[0-9]).{6,20}$|"
                            + "^(?=.*[a-z])(?=.*[$@$!%*#_*()?%*+=&]).{6,20}$|"
                            + "^(?=.*[A-Z])(?=.*[$@$!%*#_*()?%*+=&]).{6,20}$|"
                            + "^(?=.*[0-9])(?=.*[$@$!%*#_*()?%*+=&]).{6,20}$",
            message = "비밀번호 형식을 확인해주세요.\n" + "1. 8 ~ 20 글자\n" + "2. 영대소문자, 숫자, 특수문자 중 최소 2가지 이상 조합\n")
    private String password;
}
