package com.asdf148.javaproject.domain.chat.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class OldChatMessageResponse {

    private String id;

    private String message;

    private String senderName;

    private String senderImage;

    private boolean isMine;

    private List<String> readerList;

    private LocalDateTime cratedAt;

}
