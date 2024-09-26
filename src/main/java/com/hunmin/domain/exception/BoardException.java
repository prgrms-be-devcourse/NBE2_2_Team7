package com.hunmin.domain.exception;

public enum BoardException {
    NOT_FOUND("BOARD NOT_FOUND", 404),
    NOT_CREATED("BOARD NOT_CREATED", 400),
    NOT_UPDATED("BOARD NOT_UPDATED", 400),
    NOT_DELETED("BOARD NOT_DELETED", 400);

    private BoardTaskException boardTaskException;

    BoardException(String message, int code) {
        boardTaskException = new BoardTaskException(message, code);
    }

    public BoardTaskException get() {
        return boardTaskException;
    }
}
