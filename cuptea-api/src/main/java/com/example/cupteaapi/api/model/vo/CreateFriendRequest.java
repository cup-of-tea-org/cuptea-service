package com.example.cupteaapi.api.model.vo;


import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class CreateFriendRequest {

    @NotBlank(message = "친구 로그인 아이디가 비었습니다")
    private String friendLoginId;
    private String isFriend;
    private Integer blockCount;
}
