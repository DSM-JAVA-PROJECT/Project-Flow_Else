package com.asdf148.javaproject.domain.plan.service;

import com.asdf148.javaproject.domain.plan.entity.Plan;
import com.asdf148.javaproject.domain.plan.entity.PlanRepository;
import com.asdf148.javaproject.global.config.JwtToken;
import com.asdf148.javaproject.global.dto.TokenContent;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final JwtToken jwtToken;
    private final PlanRepository planRepository;

    public String completePlan(String token, ObjectId planId){
        try{
            TokenContent tokenContext = jwtToken.decodeToken(token);

            Plan plan = planRepository.findById(planId).orElseThrow();

            plan.setFinishDate(LocalDate.now());

            planRepository.save(plan);

            return "Completed";
        }catch (Exception e){
            System.out.println("PlanService completeSchedule: " + e.getMessage());
            return e.getMessage();
        }
    }
}
