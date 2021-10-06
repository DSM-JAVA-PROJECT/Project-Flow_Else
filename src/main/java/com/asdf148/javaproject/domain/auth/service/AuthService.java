package com.asdf148.javaproject.domain.auth.service;

import com.asdf148.javaproject.domain.auth.dto.*;
import com.asdf148.javaproject.domain.auth.entity.User;
import com.asdf148.javaproject.domain.auth.entity.UserRepository;
import com.asdf148.javaproject.domain.project.dto.MyPageProjects;
import com.asdf148.javaproject.domain.project.entity.Project;
import com.asdf148.javaproject.domain.project.entity.ProjectRepository;
import com.asdf148.javaproject.global.config.JwtToken;
import com.asdf148.javaproject.global.dto.ReturnToken;
import com.asdf148.javaproject.global.dto.TokenContent;
import com.asdf148.javaproject.global.redisEntity.user.VerifyUserRedisRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;

    private final ProjectRepository projectRepository;

    private final VerifyUserRedisRepository verifyUserRedisRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtToken jwtToken;

    public String signUp(SignUpUser s_user) throws Exception {

        if(userRepository.findByEmail(s_user.getEmail()).isPresent()){
            throw new Exception("이미 존재하는 이메일 입니다.");
        }

//        if (verifyUserRedisRepository.findById(s_user.getEmail()).isEmpty()) {
//            throw new Exception("인증되지 않은 이메일 입니다.");
//        }

        User user = User.builder()
                .name(s_user.getName())
                .email(s_user.getEmail())
                .password(passwordEncoder.encode(s_user.getPassword()))
                .phoneNumber(s_user.getPhone_number())
                .projects(new ArrayList<Project>())
                .build();

        userRepository.save(user);

        return "Success";
    }

    public ReturnToken signIn(SignInUser s_user) throws Exception {
        System.out.println("before findByEmail");
        User user = userRepository.findByEmail(s_user.getEmail()).orElseThrow();
        System.out.println("after findByEmail");
        if (user == null) {
            throw new Exception("이메일이 잘못되었습니다.");
        }
        if (!passwordEncoder.matches(s_user.getPassword(), user.getPassword())) {
            throw new Exception("비밀번호가 잘못되었습니다.");
        }
        return ReturnToken.builder()
                .accessToken(jwtToken.createAccessToken(user))
                .refreshToken(jwtToken.createRefreshToken(user))
                .build();
    }

    public MyPageUser myPage(String token) throws Exception{
        TokenContent tokenContext = jwtToken.decodeToken(token);

        User user = userRepository.findById(tokenContext.getId()).orElseThrow();

        MyPageUser myPageUser = MyPageUser.builder()
                .name(user.getName())
                .profileImage(user.getProfileImage())
                .projects(user.getProjects().stream().filter( project -> project.getEndDate().compareTo(LocalDate.now()) < 0 ).collect(Collectors.toList()).stream().map(
                        project -> MyPageProjects.builder()
                                .projectName(project.getProjectName())
                                .isFinished(project.isFinished())
                                .startDate(project.getStartDate())
                                .endDate(project.getEndDate())
                                .build()
                ).collect(Collectors.toList()))
                .build();

        System.out.println("myPage: " + user.getName());

        return myPageUser;
    }

    public String modifyUser(String token, String imgUrl, ModifyUser modifyUser) throws Exception{
        TokenContent tokenContext = jwtToken.decodeToken(token);

        User user = User.builder().build();

        try{
            user = userRepository.findByEmail(tokenContext.getEmail()).orElseThrow();
        }catch (Exception e){
            System.out.println("AuthService modifyUser can't find user: " + e.getMessage());
        }

        User updateUser = User.builder()
                .id(user.getId())
                .name(modifyUser.getName())
                .email(user.getEmail())
                .password(passwordEncoder.encode(modifyUser.getPassword()))
                .phoneNumber(modifyUser.getPhone_number())
                .profileImage(imgUrl)
                .projects(user.getProjects())
                .build();

        try{
            userRepository.save(updateUser);
        }catch (Exception e){
            System.out.println("AuthService modifyUser fail save: " + e.getMessage());
            throw new Exception(e.getMessage());
        }

        return "Modified";
    }

    public void addProject(String token, ObjectId projectId){
        TokenContent tokenContext = jwtToken.decodeToken(token);

        User user = userRepository.findById(tokenContext.getId()).orElseThrow();
        Project project = projectRepository.findById(projectId).orElseThrow();

        User updateUser = User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .profileImage(user.getProfileImage())
                .projects(user.getProjects())
                .build();

        updateUser.getProjects().add(project);

        userRepository.save(updateUser);
    }
}