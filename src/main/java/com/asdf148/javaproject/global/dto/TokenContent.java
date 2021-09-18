package com.asdf148.javaproject.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenContent {
    private ObjectId id;
    private String email;
}
