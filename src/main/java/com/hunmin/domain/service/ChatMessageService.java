package com.hunmin.domain.service;

import com.hunmin.domain.dto.chat.ChatMessageDTO;
import com.hunmin.domain.dto.chat.ChatMessageListRequestDTO;
import com.hunmin.domain.dto.chat.ChatMessageRequestDTO;
import com.hunmin.domain.dto.chat.MessageType;
import com.hunmin.domain.dto.page.ChatMessagePageRequestDTO;
import com.hunmin.domain.entity.ChatMessage;
import com.hunmin.domain.entity.ChatRoom;
import com.hunmin.domain.entity.Member;
import com.hunmin.domain.exception.exception.ChatMessagesException;
import com.hunmin.domain.exception.exception.ChatRoomException;
import com.hunmin.domain.pubsub.RedisSubscriber;
import com.hunmin.domain.repository.ChatMessageRepository;
import com.hunmin.domain.repository.ChatRoomRepository;
import com.hunmin.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatMessageService {

    private final RedisTemplate redisTemplate;
    private final MemberRepository memberRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final RedisSubscriber redisSubscriber;

    // destination 정보에서 roomId 추출
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1)
            return destination.substring(lastIndex + 1);
        else
            return "";
    }

    // 채팅방에 메시지 발송
    public void sendChatMessage(ChatMessageDTO chatMessageDTO) {
        Member member = memberRepository.findById(chatMessageDTO.getMemberId()).orElseThrow(ChatRoomException.NOT_FOUND::get);

        if (MessageType.ENTER.equals(chatMessageDTO.getType())) {
            chatMessageDTO.setMessage(member.getNickname() + "님이 방에 입장했습니다.");
            member.setNickname("[알림]");
        } else if (MessageType.QUIT.equals(chatMessageDTO.getType())) {
            chatMessageDTO.setMessage(member.getNickname() + "님이 방에서 나갔습니다.");
            member.setNickname("[알림]");
        }
        log.info("채팅방에서 메시지 발송 member{}", chatMessageDTO.toString());

        // 메시지 저장
        ChatMessage chatMessage = chatMessageDTO.toEntity();
        chatMessageRepository.save(chatMessage);

        ChannelTopic topic = ChannelTopic.of("" + chatMessageDTO.getChatRoomId());
        redisSubscriber.sendMessage(chatMessageDTO);
        log.info("채팅방에서 메시지 발송 topic: {}", topic.getTopic());
    }

    public List<ChatMessageDTO> readAllMessages(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(ChatRoomException.NOT_FOUND::get);
        log.info("readAllMessages ={}", chatRoom.toString());

        if (chatRoom == null) {
            // 채팅방이 없으면 빈 배열 반환
            log.info("chatRoom이 null", Collections.emptyList());
            return Collections.emptyList();
        }
        List<ChatMessageDTO> chatLists = chatRoom.getChatMessage().stream()
                .map(ChatMessageDTO::new)
                .collect(Collectors.toList());

        Collections.reverse(chatLists);
        return chatLists;
    }
    //채팅목록 페이징
    public Page<ChatMessageListRequestDTO> getList(ChatMessagePageRequestDTO chatMessagePageRequestDTO, Long chatRoomId) { //목록
        try {
            Sort sort = Sort.by("createdAt").descending();
            Pageable pageable = chatMessagePageRequestDTO.getPageable(sort);
            return chatMessageRepository.chatMessageList(pageable, chatRoomId);
        } catch (Exception e) {
            log.error("--- " + e.getMessage());
            throw ChatMessagesException.NOT_FETCHED.get();
        }
    }
}
