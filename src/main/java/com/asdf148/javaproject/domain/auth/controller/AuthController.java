package com.asdf148.javaproject.domain.auth.controller;

import com.asdf148.javaproject.domain.auth.dto.GithubToken;
import com.asdf148.javaproject.domain.auth.dto.SignInUser;
import com.asdf148.javaproject.domain.auth.dto.SignUpUser;
import com.asdf148.javaproject.domain.auth.service.AuthService;
import com.asdf148.javaproject.domain.project.dto.Image;
import com.asdf148.javaproject.global.S3Upload;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final S3Upload s3Upload;
    private final AuthService authService;
    private final HttpSession httpSession;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/oauth")
    public ResponseEntity<Object> githubLogin(HttpServletResponse response) throws IOException {
        return new ResponseEntity<Object>(new GithubToken(httpSession.getAttribute("OAuthToken").toString()), HttpStatus.OK);
    }

    @PostMapping("/join")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpUser s_user){
        try{
            return new ResponseEntity<>(authService.signUp(s_user), HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> signIn(@RequestBody SignInUser s_user){

        try{
            return new ResponseEntity<>(authService.signIn(s_user), HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println("authController /login: " + e.getMessage());
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/myPage")
    public ResponseEntity<Object> myPage(@RequestHeader Map<String, String> header){
        try{
            return new ResponseEntity<>(authService.myPage(header.get("authorization").substring(7)), HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/password")
    public ResponseEntity<String> changePassword(@RequestHeader Map<String, String> header, String password){
        try{
            return new ResponseEntity<>(authService.ChangePassword(header.get("authorization").substring(7), password), HttpStatus.OK);
        }catch (Exception e){
            System.out.println("AuthController ChangePassword: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/image")
    public ResponseEntity<Object> uploadImage(@RequestPart MultipartFile file){
        String imgUrl = "";

        try{
            imgUrl = s3Upload.upload(file, "profile");
        } catch (Exception e){
            logger.error("S3 Upload: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(Image.builder().image(imgUrl).build(), HttpStatus.OK);
    }

    @PatchMapping("/image")
    public ResponseEntity<String> ChangeImage(@RequestHeader Map<String, String> header, String image){

        try{
            return new ResponseEntity<>(authService.changeImage(header.get("authorization").substring(7), image), HttpStatus.OK);
        }catch (Exception e){
            System.out.println("AuthController modify: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
