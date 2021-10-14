package com.asdf148.javaproject.domain.plan.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MonthPlan {
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isFinish;
}
