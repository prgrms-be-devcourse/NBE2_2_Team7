package com.hunmin.domain.exception;

public enum NotificationException {
    NOT_FOUND("NOTIFICATION NOT_FOUND", 404),
    NOT_SEND("NOTIFICATION NOT_SEND", 400),
    NOT_UPDATED("NOTIFICATION NOT_UPDATED", 400);

    private NotificationTaskException notificationTaskException;

    NotificationException(String message, int code) {
        notificationTaskException = new NotificationTaskException(message, code);
    }

    public NotificationTaskException get() {
        return notificationTaskException;
    }
}
