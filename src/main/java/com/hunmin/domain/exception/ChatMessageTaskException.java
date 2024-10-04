package com.hunmin.domain.exception;

import lombok.Getter;

@Getter
public class ChatMessageTaskException extends RuntimeException {

    private String message;
    private int code;

    public ChatMessageTaskException(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
