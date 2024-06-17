package com.example.cupteaaccount.domain.join.controller.model.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class EmailCodeDto {

    private UUID emailCode;
}
