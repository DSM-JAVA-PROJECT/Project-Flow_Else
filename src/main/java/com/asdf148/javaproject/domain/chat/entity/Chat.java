package com.asdf148.javaproject.domain.chat.entity;

import com.asdf148.javaproject.domain.auth.entity.User;
import com.asdf148.javaproject.domain.chatRoom.entity.ChatRoom;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Document(collation = "chat")
public class Chat {

    @MongoId
    private ObjectId id;

    @NotBlank
    private String message;

    @DBRef
    private User sender;

    @DBRef(lazy = true)
    private ChatRoom chatRoom;

    @DBRef(lazy = true)
    private List<User> receiver;

    @CreatedDate
    private LocalDateTime createdAt;

}