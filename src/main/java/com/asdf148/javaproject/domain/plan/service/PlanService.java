package com.asdf148.javaproject.domain.plan.service;

import com.asdf148.javaproject.domain.chatRoom.entity.ChatRoom;
import com.asdf148.javaproject.domain.chatRoom.entity.ChatRoomRepository;
import com.asdf148.javaproject.domain.plan.dto.MonthPlan;
import com.asdf148.javaproject.domain.plan.dto.MonthPlans;
import com.asdf148.javaproject.domain.plan.dto.PlanDetail;
import com.asdf148.javaproject.domain.plan.dto.PlanDetails;
import com.asdf148.javaproject.domain.plan.entity.Plan;
import com.asdf148.javaproject.domain.plan.entity.PlanRepository;
import com.asdf148.javaproject.domain.project.entity.Project;
import com.asdf148.javaproject.domain.project.entity.ProjectRepository;
import com.asdf148.javaproject.global.config.JwtUtil;
import com.asdf148.javaproject.global.dto.TokenContent;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final JwtUtil jwtUtil;
    private final ProjectRepository projectRepository;
    private final ChatRoomRepository chatRoomRepository;

    public String completePlan(String token, ObjectId projectId, ObjectId planId) throws Exception {
        TokenContent tokenContext = jwtUtil.decodeToken(token);
        Project project = projectRepository.findById(projectId).orElseThrow();
        Boolean isTrue = false;
        for(ChatRoom chatRoom: project.getChatRooms()){
            for(Plan findPlan: chatRoom.getPlans()){
                if(findPlan.getId().equals(planId)){
                    System.out.println(findPlan.getFinishDate());
                    findPlan.setFinishDate(LocalDate.now());
                    isTrue = true;
                    chatRoomRepository.save(chatRoom);
                    break;
                }
            }
            if(isTrue == true){
                break;
            }
        }
        return "Completed";
    }

    public MonthPlans monthPlans(ObjectId projectId, int year, int month){

        Project project = projectRepository.findById(projectId).orElseThrow();
        List<Plan> plans = new ArrayList<>();

        for(ChatRoom chatRoom :project.getChatRooms()){
            if(chatRoom.getPlans() != null){
                plans.addAll(chatRoom.getPlans());
            }
        }

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
        List<Plan> plans = new ArrayList<>();

        for(ChatRoom chatRoom :project.getChatRooms()){
            if(chatRoom.getPlans() != null){
                for(Plan plan: chatRoom.getPlans()){
                    plans.add(plan);
                }
            }
        }

        List<PlanDetail> planDetails = new ArrayList<PlanDetail>();

        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);

        for (Plan plan: plans){

            if(plan.getStartDate().compareTo(localDate) <= 0 && plan.getEndDate().compareTo(localDate) >= 0){

                planDetails.add(PlanDetail.builder()
                        .id(plan.getId().toString())
                        .name(plan.getName())
                        .isFinish(plan.getFinishDate() != null)
                        .build());
            }
        }

        return PlanDetails.builder().planDetails(planDetails).build();
    }
}
