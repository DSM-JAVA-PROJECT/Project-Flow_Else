package com.asdf148.javaproject.domain.auth.controller;

import com.asdf148.javaproject.domain.auth.dto.SignInUser;
import com.asdf148.javaproject.domain.auth.dto.SignUpUser;
import com.asdf148.javaproject.domain.auth.service.AuthService;
import com.asdf148.javaproject.global.S3Upload;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final S3Upload s3Upload;
    private final AuthService authService;

    @GetMapping("/oauth")
    public void githubLogin(HttpServletResponse response) throws IOException {
//        return new ResponseEntity<>();
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

    @PatchMapping("/image")
    public ResponseEntity<String> ChangeImage(@RequestHeader Map<String, String> header, MultipartFile file){
        String imgUrl = "";

        try{
            imgUrl = s3Upload.upload(file, "profile");
        } catch (Exception e){
            System.out.println("AuthController modify S3 Upload: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        System.out.println("controller: " + imgUrl);

        try{
            return new ResponseEntity<>(authService.changeImage(header.get("authorization").substring(7), imgUrl), HttpStatus.OK);
        }catch (Exception e){
            System.out.println("AuthController modify: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
