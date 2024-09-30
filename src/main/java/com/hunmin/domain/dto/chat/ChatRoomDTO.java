package com.hunmin.domain.dto.chat;

import com.hunmin.domain.entity.ChatMessage;
import com.hunmin.domain.entity.ChatRoom;
import com.hunmin.domain.entity.Member;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ChatRoomDTO {
    private Long chatRoomId;
    private Long memberId; //nickname = 채팅방 이름
    private long userCount;
    private List<Long> chatMessageIds = new ArrayList<>();;

    public ChatRoomDTO(ChatRoom chatRoom) {
        this.chatRoomId = chatRoom.getChatRoomId();
        this.memberId = chatRoom.getMember().getMemberId();
        this.userCount = chatRoom.getUserCount();
        if (chatRoom.getChatMessage() != null) {
            chatRoom.getChatMessage().forEach(i -> chatMessageIds.add(i.getChatMessageId()));
        }
    }

    public ChatRoom toEntity(){
        Member member = Member.builder().memberId(memberId).build();
        List<ChatMessage> chatMessages = new ArrayList<>();
        for (Long chatMessageId : chatMessageIds) {
            chatMessages.add(ChatMessage.builder()
                    .chatMessageId(chatMessageId)
                    .build());
        }

        return ChatRoom.builder()
                .chatRoomId(chatRoomId)
                .member(member)
                .userCount(userCount)
                .chatMessage(chatMessages)
                .build();
    }
}
