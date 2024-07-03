package com.example.cupteaapi.api.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class FriendsSearchRequest {

    @NotBlank(message = "아이디가 없습니다")
    private String memberId;
}
