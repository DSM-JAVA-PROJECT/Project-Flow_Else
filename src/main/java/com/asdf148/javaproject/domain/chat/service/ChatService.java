package com.asdf148.javaproject.domain.chat.service;

import com.asdf148.javaproject.domain.auth.entity.User;
import com.asdf148.javaproject.domain.auth.entity.UserRepository;
import com.asdf148.javaproject.domain.chat.entity.Chat;
import com.asdf148.javaproject.domain.chat.entity.ChatRepository;
import com.asdf148.javaproject.domain.chat.payload.OldChatMessageListResponse;
import com.asdf148.javaproject.domain.chat.payload.OldChatMessageResponse;
import com.asdf148.javaproject.domain.chatRoom.entity.ChatRoom;
import com.asdf148.javaproject.domain.chatRoom.entity.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public OldChatMessageListResponse getOldChatMessage(String email, String chatRoomId, Pageable pageable) {
        User user = userRepository.findByEmail(email)
                .orElseThrow();
        ChatRoom chatRoom = chatRoomRepository.findById(new ObjectId(chatRoomId))
                .orElseThrow();
        return new OldChatMessageListResponse(
                chatRepository.findAllByChatRoomOrderByCreatedAtAsc(chatRoom, pageable)
                        .map(chat -> {
                            chat.getReceiver().remove(user);
                            return chat;
                        })
                        .map(chat -> buildResponse(chat, user)).getContent()
        );
    }

    private OldChatMessageResponse buildResponse(Chat chat, User user) {
        return OldChatMessageResponse.builder()
                .cratedAt(chat.getCreatedAt())
                .id(chat.getId().toString())
                .isMine(user.equals(chat.getSender()))
                .message(chat.getMessage())
                .readerList(chat.getReceiver().stream().map(User::getEmail).collect(Collectors.toList()))
                .senderImage(chat.getSender().getProfileImage())
                .senderName(chat.getSender().getName())
                .build();
    }

}
