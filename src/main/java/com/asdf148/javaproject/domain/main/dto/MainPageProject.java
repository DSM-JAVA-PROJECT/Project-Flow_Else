package com.asdf148.javaproject.domain.main.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class MainPageProject {
    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private int personalProgress;

    private int projectProgress;

    private List<MainPagePlan> mainPagePlans;
}
