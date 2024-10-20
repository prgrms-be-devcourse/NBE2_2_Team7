package com.hunmin.domain.exception;

public enum BookmarkException {
    NOT_FOUND("BOOKMARK NOT_FOUND", 404),
    NOT_CREATED("BOOKMARK NOT_CREATED", 400),
    NOT_DELETED("BOOKMARK NOT_DELETED", 400);

    private BookmarkTaskException bookmarkTaskException;

    BookmarkException(String message, int code) {
        bookmarkTaskException = new BookmarkTaskException(message, code);
    }

    public BookmarkTaskException get() {
        return bookmarkTaskException;
    }
}
