package com.hunmin.domain.exception;

public enum ChatMessageException {
    NOT_FOUND("NOT FOUND CHAT_MESSAGES", 400),
    MESSAGE_NOT_REGISTERED("CHAT_MESSAGES Not Registered", 400),
    NOT_FETCHED("CHAT_MESSAGES_LIST_PAGE NOT FETCHED", 400 );

    private final ChatMessageTaskException chatMessageTaskException;

    ChatMessageException(String message, int code) {
        this.chatMessageTaskException = new ChatMessageTaskException(message, code);
    }
    public ChatMessageTaskException get() {
        return chatMessageTaskException;
    }
}
