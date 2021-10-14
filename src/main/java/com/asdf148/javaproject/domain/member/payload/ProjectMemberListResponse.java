package com.asdf148.javaproject.domain.member.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMemberListResponse {
    private List<ProjectMemberResponse> responses;
}
