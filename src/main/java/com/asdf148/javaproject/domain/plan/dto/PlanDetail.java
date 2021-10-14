package com.asdf148.javaproject.domain.plan.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlanDetail {
    private String name;
    private Boolean isFinish;
}
