package com.asdf148.javaproject.domain.auth.dto;

import lombok.Getter;

import javax.validation.constraints.Size;

@Getter
public class ModifyUser {
    private String name;

    private String phone_number;

    //    @Pattern(regexp ="^(?=.*[a-zA-Z0-9][!@#$%^&*-])", message = "특수문자가 포함되어야 합니다.")
    @Size(min = 8, max = 16, message = "8자 이상 16자 이하여야 합니다.")
    private String password;

    private String profile_image;
}
