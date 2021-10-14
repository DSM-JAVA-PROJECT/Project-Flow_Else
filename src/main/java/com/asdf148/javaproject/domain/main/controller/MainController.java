package com.asdf148.javaproject.domain.main.controller;

import com.asdf148.javaproject.domain.main.dto.MainPageResponse;
import com.asdf148.javaproject.domain.main.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainController {

    private final MainService mainService;

    @GetMapping()
    public ResponseEntity<Object> MainPage(@RequestHeader Map<String, String> header){
        try{
            return new ResponseEntity<>(mainService.mainPage(header.get("authorization").substring(7)), HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
