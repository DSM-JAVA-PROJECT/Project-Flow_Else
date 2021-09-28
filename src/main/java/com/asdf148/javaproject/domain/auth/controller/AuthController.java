package com.asdf148.javaproject.domain.auth.controller;

import com.asdf148.javaproject.domain.auth.dto.ModifyUser;
import com.asdf148.javaproject.domain.auth.dto.SignInUser;
import com.asdf148.javaproject.domain.auth.dto.SignUpUser;
import com.asdf148.javaproject.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

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
            System.out.println(e.getMessage());
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
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/modify")
    public ResponseEntity<String> Modify(@RequestHeader Map<String, String> header, @RequestBody ModifyUser modifyUser){
        try{
            authService.modifyUser(header.get("authorization").substring(7), modifyUser);
            return new ResponseEntity<>("modified", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
