package com.asdf148.javaproject.domain.auth.controller;

import com.asdf148.javaproject.domain.auth.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    AuthService authService;

//    @Test
//    @DisplayName("회원가입")
//    void SendVerifyCode() throws Exception{
//        mvc.perform(post("/auth/join"))
//    }
}
