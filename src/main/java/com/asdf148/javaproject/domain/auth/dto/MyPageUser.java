package com.asdf148.javaproject.domain.auth.dto;

import com.asdf148.javaproject.domain.project.dto.MyPageProjects;
import com.asdf148.javaproject.domain.project.entity.Project;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyPageUser {
    private String name;
    private String profileImage;
    private List<MyPageProjects> projects;
}
