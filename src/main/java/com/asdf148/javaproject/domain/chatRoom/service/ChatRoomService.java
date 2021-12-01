package com.asdf148.javaproject.domain.chatRoom.service;

import com.asdf148.javaproject.domain.auth.entity.User;
import com.asdf148.javaproject.domain.auth.entity.UserRepository;
import com.asdf148.javaproject.domain.chatRoom.entity.ChatRoom;
import com.asdf148.javaproject.domain.chatRoom.entity.ChatRoomRepository;
import com.asdf148.javaproject.domain.chatRoom.payload.ChatRoomListResponse;
import com.asdf148.javaproject.domain.chatRoom.payload.ChatRoomResponse;
import com.asdf148.javaproject.domain.project.entity.Project;
import com.asdf148.javaproject.domain.project.entity.ProjectRepository;
import com.asdf148.javaproject.global.config.JwtUtil;
import com.asdf148.javaproject.global.dto.TokenContent;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final JwtUtil jwtUtil;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomListResponse getProjectChatRooms(ObjectId projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow();

        return new ChatRoomListResponse(project.getChatRooms()
                .stream().map(chatRoom -> ChatRoomResponse.builder()
                        .chatRoomImage(chatRoom.getRoomImage())
                        .chatRoomName(chatRoom.getName())
                        .id(chatRoom.getId().toString())
                        .build())
                .collect(Collectors.toList()));
    }

    public Project initialChatRoom(String token, Project project) {
        TokenContent tokenContext = jwtUtil.decodeToken(token);

        ChatRoom chatRoom = ChatRoom.builder()
                .name(project.getProjectName())
                .userIds(new ArrayList<>())
                .build();

        User user = userRepository.findByEmail(tokenContext.getEmail()).orElseThrow();

        chatRoom.getUserIds().add(user);

        chatRoom = chatRoomRepository.save(chatRoom);

        project.getChatRooms().add(chatRoom);

        return projectRepository.save(project);
    }
}
