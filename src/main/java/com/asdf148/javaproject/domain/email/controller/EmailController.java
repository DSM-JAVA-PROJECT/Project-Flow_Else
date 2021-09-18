package com.asdf148.javaproject.domain.email.controller;

import com.asdf148.javaproject.domain.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/") // 이메일 인증 코드 보내기
    public void emailAuth(@RequestBody Map<String, String> email) throws Exception {
        emailService.sendSimpleMessage(email.get("email"));
        return;
    }

    @PostMapping("/verifyCode") // 이메일 인증 코드 검증
    public ResponseEntity<String> verifyCode(@RequestBody Map<String, String> code) {
        try{
            return new ResponseEntity<String>(emailService.verifyCode(code.get("code")), HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }
}
