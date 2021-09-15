package com.asdf148.javaproject.domain.chatRoom.service;

import com.asdf148.javaproject.domain.auth.entity.UserRepository;
import com.asdf148.javaproject.domain.chatRoom.dto.CreateChatRoom;
import com.asdf148.javaproject.domain.chatRoom.entity.ChatRoom;
import com.asdf148.javaproject.domain.chatRoom.entity.ChatRoomRepository;
import com.asdf148.javaproject.domain.project.entity.Project;
import com.asdf148.javaproject.domain.project.entity.ProjectRepository;
import com.asdf148.javaproject.global.config.JwtToken;
import com.asdf148.javaproject.global.dto.TokenContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final JwtToken jwtToken;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    public void InitialChatRoom(String token, String projectId, CreateChatRoom createChatRoom) {
        TokenContent tokenContext = jwtToken.decodeToken(token);

        Project project = projectRepository.findById(projectId).orElseThrow();

        ChatRoom chatRoom = ChatRoom.builder()
                .name(createChatRoom.getName())
                .build();

        chatRoom.getUserIds().add(userRepository.findByEmail(tokenContext.getEmail()).orElseThrow());
        project.getChatRooms().add(chatRoom);

        Project updateProject = Project.builder()
                .id(project.getId())
                .projectName(project.getProjectName())
                .explanation(project.getExplanation())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .logoImage(project.getLogoImage())
                .chatRooms(project.getChatRooms())
                .projectUsers(project.getProjectUsers())
                .build();

        chatRoomRepository.save(chatRoom);
        projectRepository.save(updateProject);
    }
}
