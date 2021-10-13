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
    public ResponseEntity<MainPageResponse> MainPage(@RequestHeader Map<String, String> header){
        try{
            MainPageResponse mainPageResponse = MainPageResponse.builder()
                    .mainPageProjects(mainService.mainPage(header.get("authorization").substring(7)))
                    .build();

            return new ResponseEntity<>(mainPageResponse, HttpStatus.OK);
        }catch (Exception e){
            MainPageResponse mainPageResponse = MainPageResponse.builder()
                    .errorMessage(e.getMessage())
                    .build();

            System.out.println(e.getMessage());
            System.out.println(e.getClass());

            return new ResponseEntity<>(mainPageResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
