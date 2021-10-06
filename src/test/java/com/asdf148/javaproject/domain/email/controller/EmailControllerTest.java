package com.asdf148.javaproject.domain.email.controller;

import com.asdf148.javaproject.domain.auth.entity.UserRepository;
import com.asdf148.javaproject.domain.email.service.EmailService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmailController.class)
public class EmailControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    EmailService emailService;

    @MockBean
    UserRepository userRepository;

//    @Test
//    @DisplayName("이메일 인증코드 전송")
//    void SendEmailVerifyCode() throws Exception {
//
//        final ResultActions actions = mvc.perform(post("/email/")
//            .contentType(MediaType.APPLICATION_JSON)
//            .accept(MediaType.APPLICATION_JSON)
//            .characterEncoding("UTF-8")
//            .content( "{" + " \"email\" : \"test@test.com\" "+ "}"));
//
//        actions.andExpect(status().isOk());
//    }
//
//    // redis도 써야 되나?
//    @Test
//    @Disabled
//    @DisplayName("이메일 인증코드 인증")
//    void VerifyCode() throws Exception {
//
//        String verifyCode = "123456";
//
//        given(emailService.verifyCode(verifyCode)).willReturn("Success");
//
//        final ResultActions actions = mvc.perform(post("email/verifyCode")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .characterEncoding("UTF-8")
//                .content( "{" + " \"email\" : " + 123456 + "}"));
//
//        actions.andExpect(status().isOk());
//    }
}
