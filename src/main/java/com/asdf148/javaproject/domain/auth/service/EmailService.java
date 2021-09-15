package com.asdf148.javaproject.domain.auth.service;

import com.asdf148.javaproject.global.redisEntity.email.VerifyCode;
import com.asdf148.javaproject.global.redisEntity.email.VerifyCodeRedisRepository;
import com.asdf148.javaproject.global.redisEntity.user.VerifyUser;
import com.asdf148.javaproject.global.redisEntity.user.VerifyUserRedisRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {


    private final VerifyCodeRedisRepository verifyCodeRedisRepository;

    private final VerifyUserRedisRepository verifyUserRedisRepository;

    private final JavaMailSender emailSender;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public static final String ePw = createKey();

    private MimeMessage createMessage(String to)throws Exception{
        VerifyCode emailCode = new VerifyCode(to, ePw);
        verifyCodeRedisRepository.save(emailCode);

        logger.info("Send to : "+ to);
        logger.info("Authentication number : " + ePw);
        MimeMessage  message = emailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to); //보내는 대상
        message.setSubject("이메일 확인 코드: " + ePw); //제목

        String msg="";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += ePw;

        message.setText(msg, "utf-8", "html"); //내용
        message.setFrom(new InternetAddress("ji17824@gmail.com","server-test")); //보내는 사람

        return message;
    }

    // 인증코드 만들기
    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) { // 인증코드 6자리
            key.append((rnd.nextInt(10)));
        }
        return key.toString();
    }

    public void sendSimpleMessage(String to)throws Exception {
        MimeMessage message = createMessage(to);
        try{//예외처리
            emailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    public String verifyCode(String code) throws Exception {
        VerifyCode verifyCode = verifyCodeRedisRepository.findByCode(code).orElseThrow();

        if(!verifyCode.getCode().equals(code)){
            throw new Exception("인증번호가 다릅니다.");
        }

        VerifyUser verifyUser = new VerifyUser(verifyCode.getEmail());
        verifyUserRedisRepository.save(verifyUser);
        return "success";
    }
}
