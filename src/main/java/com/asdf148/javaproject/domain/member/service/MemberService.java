package com.asdf148.javaproject.domain.member.service;

import com.asdf148.javaproject.domain.auth.entity.User;
import com.asdf148.javaproject.domain.chatRoom.entity.ChatRoom;
import com.asdf148.javaproject.domain.chatRoom.entity.ChatRoomRepository;
import com.asdf148.javaproject.domain.member.payload.ProjectMemberListResponse;
import com.asdf148.javaproject.domain.member.payload.ProjectMemberResponse;
import com.asdf148.javaproject.domain.project.entity.Project;
import com.asdf148.javaproject.domain.project.entity.ProjectRepository;
import com.asdf148.javaproject.domain.project.entity.ProjectUser;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final ProjectRepository projectRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ProjectMemberListResponse getMemberList(String email, String projectId) {
        Project project = getProject(projectId);

        return new ProjectMemberListResponse(project.getProjectUsers()
                .stream()
                .map(ProjectUser::getUser)
                .filter(user -> !user.getEmail().equals(email))
                .map(this::buildMemberResponse)
                .collect(Collectors.toList()));
    }

    public ProjectMemberListResponse getNotParticipatedMemberList(String email, String projectId, String chatRoomId) {
        ChatRoom chatRoom = getChatRoom(chatRoomId);
        Project project = getProject(projectId);
        return new ProjectMemberListResponse(chatRoom.getUserIds()
                .stream()
                .filter(projectUser -> project.getProjectUsers().stream()
                        .anyMatch(member -> member.getUser().getEmail()
                                .equals(projectUser.getEmail())))
                .filter(user -> user.getEmail().equals(email))
                .map(this::buildMemberResponse)
                .collect(Collectors.toList()));
    }

    private ChatRoom getChatRoom(String chatRoomId) {
        return chatRoomRepository.findById(new ObjectId(chatRoomId))
                .orElseThrow();
    }

    private ProjectMemberResponse buildMemberResponse(User user) {
        return ProjectMemberResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .profileImage(user.getProfileImage())
                .build();
    }

    private Project getProject(String projectId) {
        return projectRepository.findById(new ObjectId(projectId))
                .orElseThrow();
    }
}
