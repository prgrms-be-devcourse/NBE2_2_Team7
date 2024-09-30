package com.hunmin.domain.exception.exception;

import lombok.Getter;

@Getter
public class ChatRoomTaskException extends RuntimeException {
    private String message;
    private int code;

    public ChatRoomTaskException(String message,int code) {
        this.message = message;
        this.code = code;
    }
}
