package com.asdf148.javaproject.domain.project.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class ProjectBody {

    @NotBlank
    private String projectName;

    private String explanation;

    @NotNull
    private String startDate;

    @NotNull
    private String endDate;

    private String image;

    private List<String> emails;
}