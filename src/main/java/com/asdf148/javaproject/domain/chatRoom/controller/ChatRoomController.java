package com.asdf148.javaproject.domain.chatRoom.controller;

import com.asdf148.javaproject.domain.chatRoom.payload.ChatRoomListResponse;
import com.asdf148.javaproject.domain.chatRoom.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/{projectId}/rooms")
    public ChatRoomListResponse getChatRoomList(@PathVariable ObjectId projectId) {
        return chatRoomService.getProjectChatRooms(projectId);
    }
}
