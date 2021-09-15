package com.asdf148.javaproject.domain.project.dto;

import com.asdf148.javaproject.domain.auth.entity.User;
import com.asdf148.javaproject.domain.chatRoom.entity.ChatRoom;
import com.asdf148.javaproject.domain.project.entity.ProjectUser;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
public class ModifyProject {
    private String projectName;
    private String explanation;
    private LocalDate startDate;
    private LocalDate endDate;
    private String logoImage;
}
