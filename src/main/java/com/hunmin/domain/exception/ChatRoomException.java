package com.hunmin.domain.exception;

public enum ChatRoomException {
    CHATROOM_ALREADY_EXIST("CHATROOM EXISTS", 400),
    NOT_FOUND("NOT FOUND CHAT_ROOM", 400),
    CHATROOM_NOT_REGISTERED("CHATROOM NOT REGISTERED", 400);


    private final ChatRoomTaskException chatRoomTaskException;

    ChatRoomException(String message, int code) {
        this.chatRoomTaskException = new ChatRoomTaskException(message, code);
    }

    public ChatRoomTaskException get() {
        return chatRoomTaskException;
    }
}
