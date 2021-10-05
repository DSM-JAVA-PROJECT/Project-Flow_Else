package com.asdf148.javaproject.domain.plan.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Plan {

    @MongoId
    private ObjectId id;

    @NotBlank
    private String name;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private LocalDate startDate;

    private LocalDate finishDate;

    private List<PlanUser> planUsers;

    @Builder
    private Plan(String name, LocalDate endDate, LocalDate startDate, LocalDate finishDate) {
        this.name = name;
        this.endDate = endDate;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.planUsers = new ArrayList<>();
    }

    public void setFinishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
    }
}