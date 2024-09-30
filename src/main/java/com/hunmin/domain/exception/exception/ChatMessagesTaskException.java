package com.hunmin.domain.exception.exception;

public class ChatMessagesTaskException extends RuntimeException {

    private String message;
    private int code;

    public ChatMessagesTaskException(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
