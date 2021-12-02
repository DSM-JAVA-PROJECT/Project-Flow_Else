package com.asdf148.javaproject.domain.plan.controller;

import com.asdf148.javaproject.domain.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plan")
public class PlanController {
    private final PlanService planService;

    @PatchMapping("/close")
    public ResponseEntity<String> completePlan(@RequestHeader Map<String, String> header, @RequestParam("project") ObjectId projectId, @RequestParam("plan") ObjectId planId){
        try{
            return new ResponseEntity<>(planService.completePlan(header.get("authorization").substring(7), projectId, planId), HttpStatus.OK);
        }catch (Exception e){
            System.out.println("PlanController completePlan: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/calendar")
    public ResponseEntity<Object> calendar(@RequestHeader Map<String, String> header, @RequestParam("id") ObjectId projectId, @RequestParam("year") int year, @RequestParam("month") int month){
        try{
            return new ResponseEntity<>(planService.monthPlans(projectId, year, month), HttpStatus.OK);
        }catch (Exception e){
            System.out.println("PlanController calendar" + e.getMessage());
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<Object> detail(@RequestHeader Map<String, String> header, @RequestParam("id") ObjectId projectId, @RequestParam("date") String date){
        try{
            return new ResponseEntity<>(planService.planDetail(projectId, date), HttpStatus.OK);
        }catch (Exception e){
            System.out.println("PlanController detail" + e.getMessage());
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
