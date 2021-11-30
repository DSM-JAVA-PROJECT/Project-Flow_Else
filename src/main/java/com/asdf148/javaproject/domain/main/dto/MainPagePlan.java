package com.asdf148.javaproject.domain.main.dto;

import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class MainPagePlan {
    private String planId;

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;
}