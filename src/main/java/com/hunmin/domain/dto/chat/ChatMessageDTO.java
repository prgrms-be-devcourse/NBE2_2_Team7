package com.hunmin.domain.dto.chat;

import com.hunmin.domain.entity.ChatMessage;
import com.hunmin.domain.entity.ChatRoom;
import com.hunmin.domain.entity.Member;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class ChatMessageDTO {
    private Long chatMessageId;
    private Long chatRoomId;
    private Long memberId;
    private String message;
    private MessageType type;

    public ChatMessageDTO(ChatMessage chatMessage) {
        this.chatMessageId = chatMessage.getChatMessageId();
        this.chatRoomId = chatMessage.getChatRoom().getChatRoomId();
        this.memberId = chatMessage.getMember().getMemberId();
        this.message = chatMessage.getMessage();
        this.type = chatMessage.getType();
    }
    public ChatMessage toEntity(){
       ChatRoom chatRoom = ChatRoom.builder().chatRoomId(chatRoomId).build();
       Member member= Member.builder().memberId(memberId).build();

       return ChatMessage.builder()
                .chatMessageId(chatMessageId)
                .chatRoom(chatRoom)
                .member(member)
                .message(message)
                .type(type)
                .build();
    }
}
