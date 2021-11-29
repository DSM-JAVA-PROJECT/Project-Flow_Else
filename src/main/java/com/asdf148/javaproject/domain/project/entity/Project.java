package com.asdf148.javaproject.domain.project.entity;

import com.asdf148.javaproject.domain.chatRoom.entity.ChatRoom;
import com.asdf148.javaproject.domain.auth.entity.User;
import com.asdf148.javaproject.domain.plan.entity.Plan;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = "project")
public class Project {

    @MongoId
    private ObjectId id;

    @NotBlank
    private String projectName;

    @NotBlank
    private String title;

    private String explanation;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotBlank
    private String logoImage;

    private List<ChatRoom> chatRooms = new ArrayList<>();

    private List<ProjectUser> projectUsers = new ArrayList<>();

    private boolean isFinished;

    @NotBlank
    @DBRef(lazy = true)
    private User pm;

    @Builder
    private Project(String projectName, String title, String explanation, LocalDate startDate, LocalDate endDate, String logoImage, User pm) {
        this.projectName = projectName;
        this.title = title;
        this.explanation = explanation;
        this.startDate = startDate;
        this.endDate = endDate;
        this.logoImage = logoImage;
        this.chatRooms = new ArrayList<>();
        this.projectUsers = new ArrayList<>();
        this.isFinished = false;
        this.pm = pm;
    }

    public boolean getIsFinished(){
        return isFinished;
    }

    public void setIsFinished (){
        isFinished = true;
    }
}