package com.asdf148.javaproject.domain.plan.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MonthPlans {
    private List<MonthPlan> monthPlans;
}
