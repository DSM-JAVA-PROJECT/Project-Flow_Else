package com.asdf148.javaproject.domain.main.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MainPage {
    private List<MainPageProject> mainPageProjects;
}
