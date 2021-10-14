package com.asdf148.javaproject.domain.plan.service;

import com.asdf148.javaproject.domain.plan.dto.MonthPlan;
import com.asdf148.javaproject.domain.plan.dto.MonthPlans;
import com.asdf148.javaproject.domain.plan.dto.PlanDetail;
import com.asdf148.javaproject.domain.plan.dto.PlanDetails;
import com.asdf148.javaproject.domain.plan.entity.Plan;
import com.asdf148.javaproject.domain.plan.entity.PlanRepository;
import com.asdf148.javaproject.domain.project.entity.Project;
import com.asdf148.javaproject.domain.project.entity.ProjectRepository;
import com.asdf148.javaproject.global.config.JwtToken;
import com.asdf148.javaproject.global.dto.TokenContent;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final JwtToken jwtToken;
    private final PlanRepository planRepository;
    private final ProjectRepository projectRepository;

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

    public MonthPlans monthPlans(ObjectId projectId, int year, int month){

        Project project = projectRepository.findById(projectId).orElseThrow();
        List<Plan> plans = project.getPlans();
        List<MonthPlan> monthPlanList = new ArrayList<MonthPlan>();

        for (Plan plan: plans){
            int planStartYear = plan.getStartDate().getYear();
            int planStartMonth = plan.getStartDate().getMonthValue();

            int planEndYear = plan.getEndDate().getYear();
            int planEndMonth = plan.getEndDate().getMonthValue();

            if(planStartYear == year && planStartMonth == month || planEndYear == year && planEndMonth == month){
                MonthPlan monthPlan = MonthPlan.builder()
                                .startDate(plan.getStartDate())
                                .endDate(plan.getEndDate())
                                .isFinish(plan.getFinishDate() != null)
                                .build();

                monthPlanList.add(monthPlan);
            }
        }

        return MonthPlans.builder()
                .monthPlans(monthPlanList)
                .build();
    }

    public PlanDetails planDetail(ObjectId projectId, String date){

        Project project = projectRepository.findById(projectId).orElseThrow();
        List<Plan> plans = project.getPlans();
        List<PlanDetail> planDetails = new ArrayList<PlanDetail>();

        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);

        for (Plan plan: plans){

            if(plan.getStartDate().compareTo(localDate) >= 0 && plan.getEndDate().compareTo(localDate) <= 0){

                planDetails.add(PlanDetail.builder()
                        .name(plan.getName())
                        .isFinish(plan.getFinishDate() != null)
                        .build());
            }
        }

        return PlanDetails.builder().planDetails(planDetails).build();
    }
}
