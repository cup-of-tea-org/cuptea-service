package com.example.cupteaaccount.common.model;

import lombok.*;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mail {

    private String setTo;
    private String subject;
    private String text;
}
