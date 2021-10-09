package com.asdf148.javaproject.domain.chatRoom.entity;

import com.asdf148.javaproject.domain.plan.entity.Plan;
import com.asdf148.javaproject.domain.auth.entity.User;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Document(collection = "chatroom")
public class ChatRoom {

    @MongoId
    private ObjectId id;

    @NotBlank
    private String name;

    @DBRef(lazy = true)
    private List<User> userIds;

    private List<Plan> plans;

    private String roomImage;
}