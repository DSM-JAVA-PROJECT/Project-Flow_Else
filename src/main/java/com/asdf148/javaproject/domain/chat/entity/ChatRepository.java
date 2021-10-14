package com.asdf148.javaproject.domain.chat.entity;

import com.asdf148.javaproject.domain.chatRoom.entity.ChatRoom;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRepository extends MongoRepository<Chat, ObjectId> {
    Page<Chat> findAllByChatRoomOrderByCreatedAtAsc(ChatRoom chatRoom, Pageable pageable);
}
