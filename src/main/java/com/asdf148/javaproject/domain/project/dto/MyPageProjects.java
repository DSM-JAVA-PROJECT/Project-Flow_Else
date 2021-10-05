package com.asdf148.javaproject.domain.project.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MyPageProjects {
    private String projectName;

    private boolean isFinished;

    private LocalDate startDate;

    private LocalDate endDate;
}
