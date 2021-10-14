package com.asdf148.javaproject.domain.chat.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class UserNotMessageOwnerException extends RuntimeException{
    private UserNotMessageOwnerException() {
        super("User Not Message Owner");
    }
}
