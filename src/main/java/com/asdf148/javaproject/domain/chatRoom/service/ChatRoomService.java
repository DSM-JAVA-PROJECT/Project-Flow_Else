package com.asdf148.javaproject.domain.chatRoom.service;

import com.asdf148.javaproject.domain.auth.entity.UserRepository;
import com.asdf148.javaproject.domain.chatRoom.entity.ChatRoom;
import com.asdf148.javaproject.domain.chatRoom.entity.ChatRoomRepository;
import com.asdf148.javaproject.domain.project.entity.Project;
import com.asdf148.javaproject.domain.project.entity.ProjectRepository;
import com.asdf148.javaproject.global.config.JwtToken;
import com.asdf148.javaproject.global.dto.TokenContent;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final JwtToken jwtToken;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    public void initialChatRoom(String token, ObjectId projectId) {
        TokenContent tokenContext = jwtToken.decodeToken(token);

        Project project = projectRepository.findById(projectId).orElseThrow();

        ChatRoom chatRoom = ChatRoom.builder()
                .name(project.getProjectName())
                .userIds(new ArrayList<>())
                .build();

        System.out.println(userRepository.findByEmail(tokenContext.getEmail()).orElseThrow());

        chatRoom.getUserIds().add(userRepository.findByEmail(tokenContext.getEmail()).orElseThrow());

        project.getChatRooms().add(chatRoom);

        chatRoomRepository.save(chatRoom);
        projectRepository.save(project);
    }
}
