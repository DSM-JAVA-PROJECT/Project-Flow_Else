package com.asdf148.javaproject.domain.auth.entity;

import com.asdf148.javaproject.domain.project.entity.Project;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Document(collation = "user")
public class User {

    @MongoId
    private String id;

    @NotBlank
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String password;

    private String profileImage;

    @NotBlank
    private String phoneNumber;

    @DBRef
    private List<Project> projects;
}