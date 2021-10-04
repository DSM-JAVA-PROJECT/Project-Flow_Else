package com.asdf148.javaproject.domain.project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CreateProject {

    @NotBlank
    private String projectName;

    private String explanation;

    @NotNull
    private String startDate;

    @NotNull
    private String endDate;

    private MultipartFile file;

    private List<String> emails;
}
