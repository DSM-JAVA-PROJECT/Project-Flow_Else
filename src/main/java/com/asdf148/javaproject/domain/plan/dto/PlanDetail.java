package com.asdf148.javaproject.domain.plan.dto;

import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;

@Getter
@Builder
public class PlanDetail {
    private String id;
    private String name;
    private Boolean isFinish;
}
