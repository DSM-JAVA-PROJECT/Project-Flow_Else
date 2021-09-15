package com.asdf148.javaproject.global.redisEntity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@RedisHash(value = "verify_user", timeToLive = 60 * 3)
public class VerifyUser {

    @Id
    private String email;
}