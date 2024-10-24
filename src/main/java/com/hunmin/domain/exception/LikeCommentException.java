package com.hunmin.domain.exception;

public enum LikeCommentException {
    NOT_FOUND("LIKECOMMENT NOT_FOUND", 404),
    NOT_CREATED("LIKECOMMENT NOT_CREATED", 400),
    NOT_DELETED("LIKECOMMENT NOT_DELETED", 400);

    private LikeCommentTaskException likeCommentTaskException;

    LikeCommentException(String message, int code) {
        likeCommentTaskException = new LikeCommentTaskException(message, code);
    }

    public LikeCommentTaskException get() {
        return likeCommentTaskException;
    }
}
