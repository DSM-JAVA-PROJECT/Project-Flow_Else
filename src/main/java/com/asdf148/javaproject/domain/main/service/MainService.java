package com.asdf148.javaproject.domain.main.service;

import com.asdf148.javaproject.domain.auth.entity.User;
import com.asdf148.javaproject.domain.auth.entity.UserRepository;
import com.asdf148.javaproject.domain.chatRoom.entity.ChatRoom;
import com.asdf148.javaproject.domain.main.dto.MainPagePlan;
import com.asdf148.javaproject.domain.main.dto.MainPageProject;
import com.asdf148.javaproject.domain.main.dto.MainPageProjects;
import com.asdf148.javaproject.domain.plan.entity.Plan;
import com.asdf148.javaproject.domain.project.entity.Project;
import com.asdf148.javaproject.global.config.JwtUtil;
import com.asdf148.javaproject.global.dto.TokenContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public MainPageProjects mainPage(String token) {

        TokenContent tokenContext = jwtUtil.decodeToken(token);

        User user = userRepository.findByEmail(tokenContext.getEmail()).orElseThrow();

        List<Project> projects = user.getProjects();
        List<List<Plan>> plansList= new ArrayList<List<Plan>>();

        List<MainPageProject> mainPageProjects = new ArrayList<MainPageProject>();

        for (Project project : projects) {
            if(project.getIsFinished() == false){
                List<Plan> plans = new ArrayList<>();
                for (ChatRoom chatRoom : project.getChatRooms()) {
                    if(chatRoom.getPlans() != null) {
                        for (Plan plan : chatRoom.getPlans()) {
                            plans.add(plan);
                        }
                    }
                }
                plansList.add(plans);
            }
        }

        List<Project> presentProjects = new ArrayList<>();

        for(int i = 0; i < projects.size(); i++ ){
            if(projects.get(i).getIsFinished() == false) {
                presentProjects.add(projects.get(i));
            }
        }

        for(int i = 0; i < presentProjects.size(); i++ ){
            if(presentProjects.get(i).getIsFinished() == false){

                MainPageProject mainPageProject = MainPageProject.builder().build();

                //진행 전
                List<MainPagePlan> before = new ArrayList<>();
                //진행 중
                List<MainPagePlan> ongoing = new ArrayList<>();
                //마감 됨
                List<MainPagePlan> after = new ArrayList<>();

                // 현재 날짜
                LocalDate current = LocalDate.now();

                float personalProgress = 0;
                float projectProgress = 0;

                for(Plan plan: plansList.get(i)){
                    if (current.isBefore(plan.getStartDate())) {
                        before.add(makePlan(plan));
                    } else if ((current.isAfter(plan.getStartDate()) || current.isEqual(plan.getStartDate())) &&
                            (current.isBefore(plan.getEndDate()) || current.isEqual((plan.getEndDate()))) &&
                            plan.getFinishDate() == null
                    ) {
                        ongoing.add(makePlan(plan));
                    } else if (plan.getFinishDate() != null && (current.isEqual(plan.getFinishDate()) || current.isAfter(plan.getFinishDate()))) {
                        after.add(makePlan(plan));
                    }
                }

                //개인별 마감된 일정, 전체 일정 수
                float personalFinish = (float) plansList.get(i).stream().filter(plan -> plan.getPlanUsers().stream().anyMatch(planUser -> planUser.getUser().equals(user)) && plan.getFinishDate() != null).count();
                float personalEntire = (float) plansList.get(i).stream().filter(plan -> plan.getPlanUsers().stream().anyMatch(planUser -> planUser.getUser().equals(user))).count();

                //전체 마감된 일정, 전체 일정 수
                float planFinish = (int) plansList.get(i).stream().filter(plan -> plan.getFinishDate() != null).count();
                float planEntire = plansList.get(i).size();

                long remaingDays = ChronoUnit.DAYS.between(LocalDate.now(), presentProjects.get(i).getEndDate());

                if (personalEntire != 0f) {
                    personalProgress = ((personalFinish / personalEntire) * 100);
                }

                if (planEntire != 0f) {
                    projectProgress = ((planFinish / planEntire) * 100);
                }

                mainPageProject = MainPageProject.builder()
                        .id(presentProjects.get(i).getId().toString())
                        .name(presentProjects.get(i).getProjectName())
                        .logoImage(presentProjects.get(i).getLogoImage())
                        .startDate(presentProjects.get(i).getStartDate())
                        .endDate(presentProjects.get(i).getEndDate())
                        .personalProgress(personalProgress)
                        .projectProgress(projectProgress)
                        .remainingDays("D-" + remaingDays)
                        .before(before)
                        .ongoing(ongoing)
                        .after(after)
                        .build();

                mainPageProjects.add(mainPageProject);
            }
        }
        return MainPageProjects.builder()
                .projects(mainPageProjects).build();
    }

    public MainPagePlan makePlan(Plan plan) {
        return MainPagePlan.builder()
                .planId(plan.getId().toString())
                .name(plan.getName())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .build();
    }
}