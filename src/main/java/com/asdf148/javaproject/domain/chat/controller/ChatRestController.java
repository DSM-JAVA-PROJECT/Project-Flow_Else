package com.asdf148.javaproject.domain.chat.controller;

import com.asdf148.javaproject.domain.chat.payload.OldChatMessageListResponse;
import com.asdf148.javaproject.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class ChatRestController {

    private final ChatService chatService;

    @GetMapping("/chat/{chatRoomId}")
    public OldChatMessageListResponse getOldMessages(@PathVariable String chatRoomId,
                                                     Pageable pageable,
                                                     @AuthenticationPrincipal Principal principal) {
        return chatService.getOldChatMessage(principal.getName(), chatRoomId, pageable);
    }
}
