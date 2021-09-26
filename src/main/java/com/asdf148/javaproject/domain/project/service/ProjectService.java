package com.asdf148.javaproject.domain.project.service;

import com.asdf148.javaproject.domain.auth.entity.UserRepository;
import com.asdf148.javaproject.domain.email.service.EmailService;
import com.asdf148.javaproject.domain.project.dto.CreateProject;
import com.asdf148.javaproject.domain.project.dto.ModifyProject;
import com.asdf148.javaproject.domain.project.entity.Project;
import com.asdf148.javaproject.domain.project.entity.ProjectRepository;
import com.asdf148.javaproject.domain.project.entity.ProjectUser;
import com.asdf148.javaproject.global.config.JwtToken;
import com.asdf148.javaproject.global.dto.TokenContent;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    private final JwtToken jwtToken;
    private final EmailService emailService;

    public String createProject(String token, CreateProject createProject){
        TokenContent tokenContext = jwtToken.decodeToken(token);

        Project project = Project.builder()
                .projectName(createProject.getProjectName())
                .explanation(createProject.getExplanation())
                .startDate(createProject.getStartDate())
                .endDate(createProject.getEndDate())
                .logoImage(createProject.getLogoImage())
                .pm(userRepository.findById(tokenContext.getId()).orElseThrow())
                .build();

        Project savedProject = projectRepository.save(project);

        initialPersonnel(token, savedProject.getId(), createProject.getField());
        for(String email: createProject.getEmails()) {
            try{
                emailService.sendInviteLink(email);
            }
            catch (Exception e){
                return e.getMessage();
            }
        }

        return "sucess";
    }

    public void initialPersonnel(String token, ObjectId projectId, String field){
        TokenContent tokenContext = jwtToken.decodeToken(token);

        Project project = projectRepository.findById(projectId).orElseThrow();

        ProjectUser projectUser = ProjectUser.builder()
                .user(userRepository.findByEmail(tokenContext.getEmail()).orElseThrow())
                .field(field)
                .build();

        project.getProjectUsers().add(projectUser);

        projectRepository.save(project);

    }

    public void addPersonnel(String email, ObjectId projectId){

        Project project = projectRepository.findById(projectId).orElseThrow();

        ProjectUser projectUser = ProjectUser.builder()
                .user(userRepository.findByEmail(email).orElseThrow())
                .build();

        project.getProjectUsers().add(projectUser);

        projectRepository.save(project);

    }

    public void modifyProject(String token, ObjectId id, ModifyProject modifyProject) throws Exception{
        TokenContent tokenContext = jwtToken.decodeToken(token);
        //프로젝트인원에서 권한확인
        if(!projectRepository.findById(id).orElseThrow().getPm().equals(userRepository.findById(tokenContext.getId()))){
            new Exception("권한이 없습니다.");
        }

        Project project = projectRepository.findById(id).orElseThrow();

        projectRepository.save(project);
    }

    public void deleteProject(String token, ObjectId id) throws Exception{
        TokenContent tokenContext = jwtToken.decodeToken(token);
        //프로젝트인원에서 권한확인
        if(!projectRepository.findById(id).orElseThrow().getPm().equals(userRepository.findById(tokenContext.getId()))){
            new Exception("권한이 없습니다.");
        }

        projectRepository.delete(projectRepository.findById(id).orElseThrow());
    }
//
//    public String addSchedule(String token, String projectId, CreateSchedule createSchedule){
//        try{
//            TokenContent tokenContext = jwtToken.decodeToken(token);
//
//            ProjectSchedule projectSchedule = projectScheduleRepository.save(
//                    new ProjectSchedule(
//                            null,
//                            createSchedule.getContent(),
//                            createSchedule.getStatus(),
//                            projectRepository.findById(Long.valueOf(projectId)).orElseThrow(),
//                            createSchedule.getStarted_at(),
//                            createSchedule.getEnded_at(),
//                            null
//                    )
//            );
//
//            joinSchedule(token, String.valueOf(projectSchedule.getId()));
//
//            return "success";
//        }catch(Exception e){
//            new Exception(e.getMessage());
//            return "error";
//        }
//    }
//
//    public String modifySchedule(String token, String scheduleId, ModifySchedule modifySchedule){
//        try {
//            TokenContent tokenContext = jwtToken.decodeToken(token);
//
//            ProjectSchedule projectSchedule = projectScheduleRepository.findById(Long.valueOf(scheduleId)).orElseThrow();
//
//            projectScheduleRepository.save(
//                    new ProjectSchedule(
//                            projectSchedule.getId(),
//                            modifySchedule.getContent(),
//                            modifySchedule.getStatus(),
//                            projectSchedule.getProject(),
//                            modifySchedule.getStarted_at(),
//                            modifySchedule.getEnded_at(),
//                            projectSchedule.getPersonnels()
//                    )
//            );
//
//            return "modified";
//
//        }catch(Exception e){
//            new Exception(e.getMessage());
//            return "error";
//        }
//    }
//
//    public String deleteSchedule(String token, String scheduleId){
//        try{
//            TokenContent tokenContext = jwtToken.decodeToken(token);
//
//            projectScheduleRepository.delete(
//                    projectScheduleRepository.findById(Long.valueOf(scheduleId)).orElseThrow()
//            );
//            return "deleted";
//        }catch (Exception e){
//            new Exception(e.getMessage());
//            return "error";
//        }
//    }
//
//    public String completeSchedule(String token, String scheduleId){
//        try{
//            TokenContent tokenContext = jwtToken.decodeToken(token);
//
//            ProjectSchedule projectSchedule = projectScheduleRepository.findById(Long.valueOf(scheduleId)).orElseThrow();
//
//            projectScheduleRepository.save(
//                    new ProjectSchedule(
//                            projectSchedule.getId(),
//                            projectSchedule.getContent(),
//                            "completed",
//                            projectSchedule.getProject(),
//                            projectSchedule.getStarted_at(),
//                            projectSchedule.getEnded_at(),
//                            projectSchedule.getPersonnels()
//                    )
//            );
//
//            return "modified";
//        }catch (Exception e){
//            new Exception(e.getMessage());
//            return "error";
//        }
//    }
//
//    public void joinSchedule(String token, String scheduleId){
//        try{
//            TokenContent tokenContext = jwtToken.decodeToken(token);
//
//            schedulePersonnelRepository.save(
//                    new SchedulePersonnel(
//                            null,
//                            projectScheduleRepository.findById(Long.valueOf(scheduleId)).orElseThrow(),
//                            userRepository.findById(tokenContext.getId()).orElseThrow()
//                    )
//            );
//        }catch (Exception e){
//            new Exception(e.getMessage());
//        }
//    }
//
//    public void leaveSchedule(String token, String scheduleId){
//        try{
//            TokenContent tokenContext = jwtToken.decodeToken(token);
//
//            schedulePersonnelRepository.delete(schedulePersonnelRepository.findById(Long.valueOf(scheduleId)).orElseThrow());
//        }catch (Exception e){
//            new Exception(e.getMessage());
//        }
//    }
//
//    public void changingRoles(String token, String role) throws Exception{
//        try{
//            TokenContent tokenContext = jwtToken.decodeToken(token);
//
//            ProjectPersonnel projectPersonnel = projectPersonnelRepository.findByUserId(tokenContext.getId()).orElseThrow();
//
//            ProjectPersonnel modifyProjectPersonnel = new ProjectPersonnel(projectPersonnel.getId(), projectPersonnel.getProject(), projectPersonnel.getUser(), projectPersonnel.getPermission(), role);
//
//            projectPersonnelRepository.save(modifyProjectPersonnel);
//        }catch (Exception e){
//            new Exception(e.getMessage());
//        }
//    }
}
