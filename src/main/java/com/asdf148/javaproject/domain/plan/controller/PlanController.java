package com.asdf148.javaproject.domain.plan.controller;

import com.asdf148.javaproject.domain.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plan")
public class PlanController {
    private final PlanService planService;

    @PatchMapping("/{id}")
    public ResponseEntity<String> completePlan(@RequestHeader Map<String, String> header, @PathVariable("id") ObjectId projectId){
        try{
            return new ResponseEntity<>(planService.completePlan(header.get("authorization").substring(7), projectId), HttpStatus.OK);
        }catch (Exception e){
            System.out.println("PlanController completePlan: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
