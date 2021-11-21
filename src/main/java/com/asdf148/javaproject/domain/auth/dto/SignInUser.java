package com.asdf148.javaproject.domain.auth.dto;

import lombok.Getter;

import javax.validation.constraints.Email;

@Getter
public class SignInUser {
    @Email
    private String email;

    private String password;

    private String deviceToken;
}
