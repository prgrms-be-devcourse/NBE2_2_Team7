package com.hunmin.domain.repository.search;

import com.hunmin.domain.dto.chat.ChatMessageListRequestDTO;
import com.hunmin.domain.entity.ChatMessage;
import com.hunmin.domain.entity.QChatMessage;
import com.hunmin.domain.entity.QChatRoom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ChatMessageSearchImpl extends QuerydslRepositorySupport implements ChatMessageSearch {
    public ChatMessageSearchImpl() {
        super(ChatMessage.class);
    }

    @Override
    public Page<ChatMessageListRequestDTO> chatMessageList(Pageable pageable, Long chatRoomId) {
        QChatMessage chatMessage = QChatMessage.chatMessage;
        QChatRoom chatRoom = QChatRoom.chatRoom;

        JPQLQuery<ChatMessage> query
                = from(chatMessage).leftJoin(chatMessage.chatRoom, chatRoom)  //조인
                .where(chatRoom.chatRoomId.eq(chatRoomId));

        JPQLQuery<ChatMessageListRequestDTO> dtoQuery = query.select(Projections.bean(
                ChatMessageListRequestDTO.class,
                chatMessage.message,
                chatMessage.createdAt,
                chatMessage.type));

        getQuerydsl().applyPagination(pageable, dtoQuery);      //페이징
        List<ChatMessageListRequestDTO> chatMessageList = dtoQuery.fetch();    //쿼리 실행
        long count = dtoQuery.fetchCount();         //레코드 수 조회

        return new PageImpl<>(chatMessageList, pageable, count);
    }
}
