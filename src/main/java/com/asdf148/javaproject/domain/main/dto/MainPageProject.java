package com.asdf148.javaproject.domain.main.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class MainPageProject {
    private String id;

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private int personalProgress;

    private int projectProgress;

    private List<MainPagePlan> mainPagePlans;
}
