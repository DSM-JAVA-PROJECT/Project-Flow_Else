package com.asdf148.javaproject.domain.main.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MainPageResponse {
    private List<MainPageProject> mainPageProjects;
    private String errorMessage;
}
