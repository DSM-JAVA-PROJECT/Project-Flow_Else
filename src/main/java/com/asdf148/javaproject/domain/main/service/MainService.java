package com.asdf148.javaproject.domain.main.service;

import com.asdf148.javaproject.domain.auth.entity.User;
import com.asdf148.javaproject.domain.auth.entity.UserRepository;
import com.asdf148.javaproject.domain.chatRoom.entity.ChatRoom;
import com.asdf148.javaproject.domain.main.dto.MainPagePlan;
import com.asdf148.javaproject.domain.main.dto.MainPageProject;
import com.asdf148.javaproject.domain.main.dto.MainPageUser;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public List<MainPageProject> mainPage(String token){

        TokenContent tokenContext = jwtUtil.decodeToken(token);

        System.out.println(tokenContext.getId());

        User user = userRepository.findByEmail(tokenContext.getEmail()).orElseThrow();

        List<Project> projects = user.getProjects();
        List<List<Plan>> plans = new ArrayList<List<Plan>>();

        List<MainPageProject> mainPageProjects = new ArrayList<MainPageProject>();

        for (Project project: projects) {
            for(ChatRoom chatRoom: project.getChatRooms()){
                plans.add(chatRoom.getPlans());
            }
        }

        for (Project project: projects) {
            MainPageProject mainPageProject = MainPageProject.builder().build();

            List<MainPagePlan> before = new ArrayList<>();
            List<MainPagePlan> ongoing = new ArrayList<>();
            List<MainPagePlan> after = new ArrayList<>();

            LocalDate current = LocalDate.now();

            for (List<Plan> planList: plans){
                for( Plan plan: planList){
                    if(current.isBefore(plan.getStartDate())){
                        before.add(makePlan(plan));
                    }
                    else if((current.isAfter(plan.getStartDate()) || current.isEqual(plan.getStartDate())) &&
                            (current.isBefore(plan.getEndDate()) || current.isEqual((plan.getEndDate()))) &&
                            plan.getFinishDate() == null
                    ){
                        ongoing.add(makePlan(plan));
                    }
                    else if(plan.getFinishDate() != null && (current.isEqual(plan.getFinishDate()) || current.isAfter(plan.getFinishDate()))){
                        after.add(makePlan(plan));
                    }
                }
            }

            for (List<Plan> planList: plans){

                int personalFinish = (int) planList.stream().filter(plan -> plan.getPlanUsers().stream().anyMatch(planUser -> planUser.getUser().equals(user))).count();
                int personalEntire = (int) planList.stream().filter(plan -> plan.getPlanUsers().stream().anyMatch(planUser -> planUser.getUser().equals(user)) && plan.getFinishDate() != null).count();

                int planFinish = planList.size();
                int planEntire = (int) planList.stream().filter(plan -> plan.getFinishDate() != null).count();

                int personalProgress = 0;
                int projectProgress = 0;

                long remaingDays = ChronoUnit.DAYS.between(project.getStartDate(), project.getEndDate());

                try{
                    personalProgress = personalFinish / personalEntire;
                }catch (ArithmeticException e){
                    personalProgress = 0;
                }

                try{
                    projectProgress = planFinish / planEntire;
                }catch (ArithmeticException e){
                    projectProgress = 0;
                }
                 mainPageProject = MainPageProject.builder()
                        .id(project.getId().toString())
                        .name(project.getProjectName())
                        .logoImage(project.getLogoImage())
                        .startDate(project.getStartDate())
                        .endDate(project.getEndDate())
                        .personalProgress(""+personalProgress+"%")
                        .projectProgress(""+projectProgress+"%")
                        .remainingDays("D-"+remaingDays)
                        .before(before)
                        .ongoing(ongoing)
                        .after(after)
                        .build();
            }
            mainPageProjects.add(mainPageProject);
        }
        return mainPageProjects;
    }

    public MainPagePlan makePlan(Plan plan){
        return MainPagePlan.builder()
                .name(plan.getName())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .mainPageUsers(plan.getPlanUsers().stream().map(
                        planUser -> MainPageUser.builder()
                                .image(planUser.getUser().getProfileImage())
                                .build()
                ).collect(Collectors.toList()))
                .build();
    }
}