package com.asdf148.javaproject.domain.auth.service;

import com.asdf148.javaproject.domain.auth.dto.*;
import com.asdf148.javaproject.domain.auth.entity.User;
import com.asdf148.javaproject.domain.auth.entity.UserRepository;
import com.asdf148.javaproject.domain.project.dto.MyPageProjects;
import com.asdf148.javaproject.domain.project.entity.Project;
import com.asdf148.javaproject.domain.project.entity.ProjectRepository;
import com.asdf148.javaproject.global.config.JwtUtil;
import com.asdf148.javaproject.global.dto.ReturnToken;
import com.asdf148.javaproject.global.dto.TokenContent;
import com.asdf148.javaproject.global.redisEntity.user.VerifyUserRedisRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;

    private final ProjectRepository projectRepository;

    private final VerifyUserRedisRepository verifyUserRedisRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    @Value("${oauth2.authorizedRedirectUris}")
    private String oauthRedirect;

    @Value("${spring.data.mongodb.uri}")
    private String mongoDBURI;

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

        User user = userRepository.findByEmail(s_user.getEmail()).orElseThrow();
        if (user == null) {
            throw new Exception("이메일이 잘못되었습니다.");
        }
        if (!passwordEncoder.matches(s_user.getPassword(), user.getPassword())) {
            throw new Exception("비밀번호가 잘못되었습니다.");
        }

        user.setDeviceToken(s_user.getDeviceToken());
        userRepository.save(user);

        return ReturnToken.builder()
                .accessToken(jwtUtil.createAccessToken(user))
                .refreshToken(jwtUtil.createRefreshToken(user))
                .build();
    }

    public MyPageUser myPage(String token) throws Exception{
        TokenContent tokenContext = jwtUtil.decodeToken(token);

        User user = userRepository.findById(tokenContext.getId()).orElseThrow();

        MyPageUser myPageUser = MyPageUser.builder()
                .name(user.getName())
                .profileImage(user.getProfileImage())
                .projects(user.getProjects().stream().filter( project -> project.getIsFinished() ).collect(Collectors.toList()).stream().map(
                        project -> MyPageProjects.builder()
                                .projectName(project.getProjectName())
                                .logoImage(project.getLogoImage())
                                .isFinished(project.getIsFinished())
                                .startDate(project.getStartDate())
                                .endDate(project.getEndDate())
                                .build()
                ).collect(Collectors.toList()))
                .build();

        return myPageUser;
    }

    public String ChangePassword(String token, String password) throws Exception{
        TokenContent tokenContext = jwtUtil.decodeToken(token);

        User user = userRepository.findById(tokenContext.getId()).orElseThrow();

        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);

        return "Changed";
    }

    public String changeImage(String token, String imgUrl) throws Exception{
        TokenContent tokenContext = jwtUtil.decodeToken(token);

        User user = User.builder().build();

        try{
            user = userRepository.findByEmail(tokenContext.getEmail()).orElseThrow();
        }catch (Exception e){
            System.out.println("AuthService changeImage can't find user: " + e.getMessage());
        }

        user.setProfileImage(imgUrl);

        try{
            userRepository.save(user);
        }catch (Exception e){
            System.out.println("AuthService changeImage fail save: " + e.getMessage());
            throw new Exception(e.getMessage());
        }

        return "Changed";
    }

    public void addProject(String token, Project project){
        TokenContent tokenContext = jwtUtil.decodeToken(token);

        User user = userRepository.findByEmail(tokenContext.getEmail()).orElseThrow();

        User updateUser = User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .profileImage(user.getProfileImage())
                .projects(user.getProjects())
                .build();

        try{
            updateUser.getProjects().add(project);
        }catch (NullPointerException e){
            updateUser = User.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .phoneNumber(user.getPhoneNumber())
                    .profileImage(user.getProfileImage())
                    .projects(new ArrayList<Project>())
                    .build();

            updateUser.getProjects().add(project);
        }

        userRepository.save(updateUser);
    }
}