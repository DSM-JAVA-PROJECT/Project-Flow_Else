package com.asdf148.javaproject.domain.project.entity;

import com.asdf148.javaproject.domain.auth.entity.User;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ProjectUser {

    @DBRef(lazy = true)
    @Field(name = "userId")
    private User user;

    private String field;

}