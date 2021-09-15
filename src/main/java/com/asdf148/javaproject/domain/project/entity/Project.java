package com.asdf148.javaproject.domain.project.entity;

import com.asdf148.javaproject.domain.chatRoom.entity.ChatRoom;
import com.asdf148.javaproject.domain.auth.entity.User;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Document(collation = "project")
public class Project {

    @MongoId
    private String id;

    @NotBlank
    private String projectName;

    private String explanation;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotBlank
    private String logoImage;

    @DBRef(lazy = true)
    private List<ChatRoom> chatRooms;

    @DBRef(lazy = true)
    private List<ProjectUser> projectUsers;

    private boolean isFinished;

    @NotBlank
    @DBRef(lazy = true)
    private User pm;

}