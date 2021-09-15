package com.asdf148.javaproject.global.redisEntity.email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
@RedisHash(value = "verify_code", timeToLive = 60 * 3)
public class VerifyCode {

    @Id
    private String email;

    @Indexed
    private String code;
}
