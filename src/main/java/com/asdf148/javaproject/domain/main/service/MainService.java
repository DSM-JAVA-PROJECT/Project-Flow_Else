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

        System.out.println(tokenContext.getId());

        User user = userRepository.findByEmail(tokenContext.getEmail()).orElseThrow();

        List<Project> projects = user.getProjects();
        List<Plan> plans = new ArrayList<>();

        List<MainPageProject> mainPageProjects = new ArrayList<MainPageProject>();

        for (Project project : projects) {
            for (ChatRoom chatRoom : project.getChatRooms()) {
                if(chatRoom.getPlans() != null) {
                    for (Plan plan : chatRoom.getPlans()) {
                        plans.add(plan);
                    }
                }
            }
        }

        for (Project project : projects) {
            if(project.getIsFinished() == false){
                MainPageProject mainPageProject = MainPageProject.builder().build();

                //진행 전
                List<MainPagePlan> before = new ArrayList<>();
                //진행 중
                List<MainPagePlan> ongoing = new ArrayList<>();
                //마감 됨
                List<MainPagePlan> after = new ArrayList<>();

                // 현재 날짜
                LocalDate current = LocalDate.now();

                // 일정 분류
                for (Plan plan : plans) {
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
                int personalFinish = (int) plans.stream().filter(plan -> plan.getPlanUsers().stream().anyMatch(planUser -> planUser.getUser().equals(user)) && plan.getFinishDate() != null).count();
                int personalEntire = (int) plans.stream().filter(plan -> plan.getPlanUsers().stream().anyMatch(planUser -> planUser.getUser().equals(user))).count();

                //전체 마감된 일정, 전체 일정 수
                int planFinish = (int) plans.stream().filter(plan -> plan.getFinishDate() != null).count();
                int planEntire = plans.size();

                int personalProgress = 0;
                int projectProgress = 0;

                long remaingDays = ChronoUnit.DAYS.between(LocalDate.now(), project.getEndDate());

                try {
                    personalProgress = personalFinish / personalEntire;
                } catch (ArithmeticException e) {
                    personalProgress = 0;
                }

                try {
                    projectProgress = planFinish / planEntire;
                } catch (ArithmeticException e) {
                    projectProgress = 0;
                }
                mainPageProject = MainPageProject.builder()
                        .id(project.getId().toString())
                        .name(project.getProjectName())
                        .logoImage(project.getLogoImage())
                        .startDate(project.getStartDate())
                        .endDate(project.getEndDate())
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