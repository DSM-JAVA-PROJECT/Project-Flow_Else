package com.asdf148.javaproject.domain.auth.entity;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    Optional<User> findById(ObjectId id);
    Optional<User> findByEmail(String email);
}

//public interface UserRepository extends MongoRepository<User, String> {
//    @Query("SELECT u FROM User u WHERE u.email = ?1")
//    User findByEmail(String email);
//
//    Optional<User> findById(ObjectId id);
//}
