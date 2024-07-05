package com.example.cupteaapi.api.model.vo;

import com.example.db.domain.model.dto.CreateFriendDto;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class CreateFriendResponse {

    private String friendLoginId;
}
