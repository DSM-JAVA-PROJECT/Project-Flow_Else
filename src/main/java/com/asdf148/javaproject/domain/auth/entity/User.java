package com.asdf148.javaproject.domain.auth.entity;

import com.asdf148.javaproject.domain.project.entity.Project;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Document(collection = "user")
public class User {

    @MongoId
    private ObjectId id;

    @NotBlank
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String password;

    private String profileImage;

    @NotBlank
    private String phoneNumber;

    @DBRef(lazy = true)
    private List<Project> projects;
}