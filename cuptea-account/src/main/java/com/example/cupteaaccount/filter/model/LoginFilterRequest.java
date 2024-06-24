package com.example.cupteaaccount.filter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginFilterRequest {

    private String loginId;
    private String password;
}
