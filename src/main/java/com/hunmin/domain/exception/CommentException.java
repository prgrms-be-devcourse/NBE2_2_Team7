package com.hunmin.domain.exception;

public enum CommentException {
    NOT_FOUND("COMMENT NOT_FOUND", 404),
    NOT_CREATED("COMMENT NOT_CREATED", 400),
    NOT_UPDATED("COMMENT NOT_UPDATED", 400),
    NOT_DELETED("COMMENT NOT_DELETED", 400);

    private CommentTaskException commentTaskException;

    CommentException(String message, int code) {
        commentTaskException = new CommentTaskException(message, code);
    }

    public CommentTaskException get() {
        return commentTaskException;
    }
}
