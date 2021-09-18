package com.asdf148.javaproject.domain.plan.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Plan {

    @MongoId
    private ObjectId id;

    @NotBlank
    private String name;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate finishDate;

    @DBRef(lazy = true)
    private List<PlanUser> planUsers;

}