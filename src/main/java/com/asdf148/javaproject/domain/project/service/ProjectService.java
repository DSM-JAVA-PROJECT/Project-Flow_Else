package com.asdf148.javaproject.domain.project.service;

import com.asdf148.javaproject.domain.auth.entity.User;
import com.asdf148.javaproject.domain.auth.entity.UserRepository;
import com.asdf148.javaproject.domain.auth.service.AuthService;
import com.asdf148.javaproject.domain.chatRoom.service.ChatRoomService;
import com.asdf148.javaproject.domain.email.service.EmailService;
import com.asdf148.javaproject.domain.project.dto.CreateProject;
import com.asdf148.javaproject.domain.project.dto.ModifyProject;
import com.asdf148.javaproject.domain.project.dto.ProjectBody;
import com.asdf148.javaproject.domain.project.entity.Project;
import com.asdf148.javaproject.domain.project.entity.ProjectRepository;
import com.asdf148.javaproject.domain.project.entity.ProjectUser;
import com.asdf148.javaproject.global.config.JwtUtil;
import com.asdf148.javaproject.global.dto.TokenContent;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final ChatRoomService chatRoomService;
    private final AuthService authService;

    public String createProject(String token, String imgUrl, CreateProject createProject) throws Exception {
        TokenContent tokenContext = jwtUtil.decodeToken(token);

        Project project = Project.builder()
                .projectName(createProject.getProjectName())
                .explanation(createProject.getExplanation())
                .startDate(LocalDate.parse(createProject.getStartDate(), DateTimeFormatter.ISO_DATE))
                .endDate(LocalDate.parse(createProject.getEndDate(), DateTimeFormatter.ISO_DATE))
                .logoImage(imgUrl)
                .pm(userRepository.findById(tokenContext.getId()).orElseThrow())
                .build();

        Project savedProject = projectRepository.save(project);

        //???????????? ?????? ?????? ??????
        savedProject = initialPersonnel(token, savedProject);

        try {
            //???????????? ?????? ????????? ??????
            savedProject = chatRoomService.initialChatRoom(token, savedProject);
        }catch (Exception e){
            System.out.println("initialCheatRoom Fail: " + e.getMessage());
            return e.getMessage();
        }

        //????????? ???????????? ??????
        authService.addProject(token, savedProject);

        for(String email: createProject.getEmails()) {
            try{
                //????????? ??????
                emailService.sendInviteLink(email, savedProject.getId());
            }
            catch (Exception e){
                System.out.println("sendInviteLink" + e.getMessage());
                return e.getMessage();
            }
        }

        return savedProject.getId().toString();
    }

    public String temporaryCreateProject(String token,
                                         String imgUrl,
                                         String projectName,
                                         String explanation,
                                         String startDate,
                                         String endDate,
                                         String emails) throws Exception {
        TokenContent tokenContext = jwtUtil.decodeToken(token);

        Project project = Project.builder()
                .projectName(projectName)
                .explanation(explanation)
                .startDate(LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE))
                .endDate(LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE))
                .logoImage(imgUrl)
                .pm(userRepository.findById(tokenContext.getId()).orElseThrow())
                .build();

        Project savedProject = projectRepository.save(project);

        //???????????? ?????? ?????? ??????
        savedProject = initialPersonnel(token, savedProject);

        try {
            //???????????? ?????? ????????? ??????
            savedProject = chatRoomService.initialChatRoom(token, savedProject);
        }catch (Exception e){
            System.out.println("initialCheatRoom Fail: " + e.getMessage());
            return e.getMessage();
        }

        //????????? ???????????? ??????
        authService.addProject(token, savedProject);

        String[] emailList = emails.split(",");

        for(String email: emailList) {
            try{
                //????????? ??????
                emailService.sendInviteLink(email, savedProject.getId());
            }
            catch (Exception e){
                System.out.println("sendInviteLink" + e.getMessage());
                return e.getMessage();
            }
        }

        return savedProject.getId().toString();
    }

    public String testCreateProject(String token, ProjectBody projectBody) throws Exception {
        TokenContent tokenContext = jwtUtil.decodeToken(token);

        Project project = Project.builder()
                .projectName(projectBody.getProjectName())
                .explanation(projectBody.getExplanation())
                .startDate(LocalDate.parse(projectBody.getStartDate(), DateTimeFormatter.ISO_DATE))
                .endDate(LocalDate.parse(projectBody.getEndDate(), DateTimeFormatter.ISO_DATE))
                .logoImage(projectBody.getImage())
                .pm(userRepository.findById(tokenContext.getId()).orElseThrow())
                .build();

        Project savedProject = projectRepository.save(project);

        //???????????? ?????? ?????? ??????
        savedProject = initialPersonnel(token, savedProject);

        try {
            //???????????? ?????? ????????? ??????
            savedProject = chatRoomService.initialChatRoom(token, savedProject);
        }catch (Exception e){
            System.out.println("initialCheatRoom Fail: " + e.getMessage());
            return e.getMessage();
        }

        //????????? ???????????? ??????
        authService.addProject(token, savedProject);

//        String[] emailList = emails.split(",");

        for(String email: projectBody.getEmails()) {
            try{
                //????????? ??????
                emailService.sendInviteLink(email, savedProject.getId());
            }
            catch (Exception e){
                System.out.println("sendInviteLink" + e.getMessage());
                return e.getMessage();
            }
        }

        return savedProject.getId().toString();
    }

    public Project initialPersonnel(String token, Project project) {
        TokenContent tokenContext = jwtUtil.decodeToken(token);

        ProjectUser projectUser = ProjectUser.builder()
                .user(userRepository.findByEmail(tokenContext.getEmail()).orElseThrow())
                .build();

        project.getProjectUsers().add(projectUser);

        return projectRepository.save(project);
    }

    public void addPersonnel(String email, ObjectId projectId){

        Project project = projectRepository.findById(projectId).orElseThrow();

        User user = userRepository.findByEmail(email).orElseThrow();

        ProjectUser projectUser = ProjectUser.builder()
                .user(user)
                .build();

        project.getProjectUsers().add(projectUser);

        project = projectRepository.save(project);

        user.getProjects().add(project);

        userRepository.save(user);
    }

    public String modifyProject(String token, ObjectId id, String imgUrl, ModifyProject modifyProject) throws Exception{
        TokenContent tokenContext = jwtUtil.decodeToken(token);
        //???????????????????????? ????????????
        if(!(projectRepository.findById(id).orElseThrow().getPm().getId().toString().equals(tokenContext.getId().toString()))){
            throw new Exception("????????? ????????????.");
        }

        try{
            Project project = projectRepository.findById(id).orElseThrow();
        }catch (Exception e){
            System.out.println("ProjectService modifyProject can't find project: " + e.getMessage());
        }

        Project updateProject = Project.builder()
                .projectName(modifyProject.getProjectName())
                .explanation(modifyProject.getExplanation())
                .startDate(LocalDate.parse(modifyProject.getStartDate(), DateTimeFormatter.ISO_DATE))
                .endDate(LocalDate.parse(modifyProject.getStartDate(), DateTimeFormatter.ISO_DATE))
                .logoImage(imgUrl)
                .build();


        try{
            projectRepository.save(updateProject);
        }catch (Exception e){
            System.out.println("Fail save");
            throw new Exception(e.getMessage());
        }

        return "Modified";
    }

    public String deleteProject(String token, ObjectId projectId) throws Exception{
        TokenContent tokenContext = jwtUtil.decodeToken(token);

        Project project = projectRepository.findById(projectId).orElseThrow();

        //???????????????????????? ????????????
        if(!(project.getPm().getId().toString().equals(tokenContext.getId().toString()))){
            throw new Exception("????????? ????????????.");
        }
        try{
            projectRepository.delete(project);
        }catch (Exception e){
            return e.getMessage();
        }

        return "Success";
    }

    public String closeProject(String token, ObjectId projectId) throws Exception{
        TokenContent tokenContext = jwtUtil.decodeToken(token);

        Project project = projectRepository.findById(projectId).orElseThrow();

        if(!(project.getPm().getId().toString().equals(tokenContext.getId().toString()))){
            throw new Exception("????????? ????????????.");
        }

        project.setIsFinished();

        projectRepository.save(project);

        return "Project closed";
    }
}
