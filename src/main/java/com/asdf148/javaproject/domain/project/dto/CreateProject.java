package com.asdf148.javaproject.domain.project.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CreateProject {

    @NotBlank
    private String projectName;

    private String explanation;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private MultipartFile file;

    private List<String> email;
}
