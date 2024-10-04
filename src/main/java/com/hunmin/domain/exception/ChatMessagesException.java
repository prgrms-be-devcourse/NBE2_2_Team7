package com.hunmin.domain.exception;

public enum ChatMessagesException {
    NOT_FOUND("NOT FOUND CHAT_MESSAGES", 400),
    MESSAGE_NOT_REGISTERED("CHAT_MESSAGES Not Registered", 400),
    NOT_FETCHED("CHAT_MESSAGES_LIST_PAGE NOT FETCHED", 400 );

    private final ChatMessagesTaskException chatMessagesTaskException;

    ChatMessagesException(String message, int code) {
        this.chatMessagesTaskException = new ChatMessagesTaskException(message, code);
    }
    public ChatMessagesTaskException get() {
        return chatMessagesTaskException;
    }
}
