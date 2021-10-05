package com.asdf148.javaproject.domain.project.dto;

import com.asdf148.javaproject.domain.auth.entity.User;
import com.asdf148.javaproject.domain.chatRoom.entity.ChatRoom;
import com.asdf148.javaproject.domain.project.entity.ProjectUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ModifyProject {
    private String projectName;
    private String explanation;
    private String startDate;
    private String endDate;
    private MultipartFile file;
}
