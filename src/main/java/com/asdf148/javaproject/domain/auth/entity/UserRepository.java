package com.asdf148.javaproject.domain.auth.entity;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}

//public interface UserRepository extends MongoRepository<User, String> {
//    @Query("SELECT u FROM User u WHERE u.email = ?1")
//    User findByEmail(String email);
//
//    Optional<User> findById(ObjectId id);
//}
