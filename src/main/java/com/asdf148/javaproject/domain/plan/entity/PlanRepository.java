package com.asdf148.javaproject.domain.plan.entity;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlanRepository extends MongoRepository<Plan, ObjectId> {
}
