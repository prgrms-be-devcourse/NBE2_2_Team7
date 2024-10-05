package com.hunmin.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.EAGER) // @JoinColumn 제거
    private List<ChatMessage> chatMessage;

    private long userCount=1;

    public void add(ChatMessage chatMessage){
        this.chatMessage.add(chatMessage);
        chatMessage.setChatRoom(this);
    }
}
