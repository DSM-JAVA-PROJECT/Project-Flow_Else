package com.asdf148.javaproject.domain.chatRoom.entity;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
}
