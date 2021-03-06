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

    private String logoImage;

    private LocalDate startDate;

    private LocalDate endDate;

    private float personalProgress;

    private float projectProgress;

    private String remainingDays;

    private List<MainPagePlan> before;

    private List<MainPagePlan> ongoing;

    private List<MainPagePlan> after;
}
