package com.hunmin.domain.service;

import com.hunmin.domain.dto.chat.ChatMessageDTO;
import com.hunmin.domain.dto.chat.ChatMessageListRequestDTO;
import com.hunmin.domain.dto.chat.MessageType;
import com.hunmin.domain.dto.page.ChatMessagePageRequestDTO;
import com.hunmin.domain.entity.ChatMessage;
import com.hunmin.domain.entity.ChatRoom;
import com.hunmin.domain.entity.Member;
import com.hunmin.domain.exception.ChatMessageException;
import com.hunmin.domain.exception.ChatRoomException;
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

    //roomId 찾기
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
    //모든 채팅기록 조회
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
            log.error("쳇서비스 페이징 실패 ={}",e.getMessage());
            throw ChatMessageException.NOT_FETCHED.get();
        }
    }
    //채팅 조회
    public ChatMessageDTO readChatMessage(Long chatMessageId) {
        return new ChatMessageDTO(chatMessageRepository.findById(chatMessageId)
                .orElseThrow(ChatMessageException.NOT_FOUND::get));
    }
    //채팅 수정
    public ChatMessageDTO updateChatMessage(ChatMessageDTO chatMessageDTO) {
        ChatMessage foundChatMessage = chatMessageRepository.findById(chatMessageDTO.getChatMessageId())
                .orElseThrow(ChatMessageException.NOT_FOUND::get);

        foundChatMessage.setMessage(chatMessageDTO.getMessage());
        return new ChatMessageDTO(chatMessageRepository.save(foundChatMessage));
    }
    //채팅 삭제
    public Boolean deleteChatMessage(Long chatMessageId) {
        ChatMessage chatMessage= chatMessageRepository.findById(chatMessageId)
                .orElseThrow(ChatMessageException.NOT_FOUND::get);
            chatMessageRepository.deleteById(chatMessageId);
            return true;
    }
}
