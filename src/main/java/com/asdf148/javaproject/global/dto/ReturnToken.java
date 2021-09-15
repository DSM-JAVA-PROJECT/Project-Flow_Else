package com.asdf148.javaproject.global.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReturnToken {
    private String accessToken;
    private String refreshToken;
}
