package com.hunmin.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hunmin.domain.dto.chat.ChatRoomDTO;
import com.hunmin.domain.dto.chat.ChatRoomRequestDTO;
import com.hunmin.domain.dto.notification.NotificationSendDTO;
import com.hunmin.domain.entity.ChatRoom;
import com.hunmin.domain.entity.Member;
import com.hunmin.domain.entity.NotificationType;
import com.hunmin.domain.exception.ChatRoomException;
import com.hunmin.domain.exception.MemberException;
import com.hunmin.domain.handler.SseEmitters;
import com.hunmin.domain.repository.ChatRoomRepository;
import com.hunmin.domain.repository.MemberRepository;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatRoomService {

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, ChatRoomRequestDTO> roomStorage;
    private final ObjectMapper objectMapper;

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    private final NotificationService notificationService;
    private final SseEmitters sseEmitters;

    // 단일 채팅방 조회
    public ChatRoomDTO findRoomById(Long id) {
        ChatRoom chatRoom = chatRoomRepository.findById(id).orElseThrow(ChatRoomException.NOT_FOUND::get);
        return new ChatRoomDTO(chatRoom);
    }

    // 나랑 관련된 모든 채팅방 조회
    public List<ChatRoomRequestDTO> findRoomByEmail(String email) {
        Member me = memberRepository.findByEmail(email);
        List<ChatRoomRequestDTO> partnerNameAndChatRoom = roomStorage.values(me.getNickname());
        log.info("partnerNameAndChatRoom={}", partnerNameAndChatRoom);

        List<ChatRoomRequestDTO> chatRoomRequestDTOList = new ArrayList<>();
        Set<Long> chatRoomIdInSet = new HashSet<>();
        for (Object rawChatRoom : partnerNameAndChatRoom) {
            ChatRoomRequestDTO chatRoomRequestDTO;
            if (rawChatRoom instanceof LinkedHashMap) {
                chatRoomRequestDTO = objectMapper.convertValue(rawChatRoom, ChatRoomRequestDTO.class);
            } else {
                chatRoomRequestDTO = (ChatRoomRequestDTO) rawChatRoom;
            }
            chatRoomIdInSet.add(chatRoomRequestDTO.getChatRoomId());
            chatRoomRequestDTOList.add(chatRoomRequestDTO);
            log.info("chatRoomIdInSet={}",chatRoomIdInSet);
            log.info("chatRoomRequestDTOList={}",chatRoomRequestDTOList);

        }

        Set<String> partnerNames = roomStorage.keys(me.getNickname());
        for (String partnerName : partnerNames) {
            log.info("partnerName={}", partnerName);
            Object rawChatRoom = roomStorage.get(partnerName, me.getNickname());
            if (rawChatRoom != null) {
                ChatRoomRequestDTO chatRoomRequestDTO;
                log.info("rawChatRoom={}", rawChatRoom);
                if (rawChatRoom instanceof LinkedHashMap) {
                    chatRoomRequestDTO = objectMapper.convertValue(rawChatRoom, ChatRoomRequestDTO.class);
                } else {
                    chatRoomRequestDTO = (ChatRoomRequestDTO) rawChatRoom;
                    log.info("chatRoomRequestDTO={}", chatRoomRequestDTO);
                }
                if (!chatRoomIdInSet.contains(chatRoomRequestDTO.getChatRoomId())){
                    chatRoomRequestDTOList.add(chatRoomRequestDTO);
                }
            }
        }
        return chatRoomRequestDTOList;
    }

    // 채팅방 생성 : 이름으로 상대방 검색 후 채팅방 개설  -> 새로운
    public ChatRoomDTO createChatRoomByNickName(String partnerName, String myEmail) {
        Member me = memberRepository.findByEmail(myEmail);
        log.info("me ={}", me);
        ChatRoomRequestDTO chatRoomFromStorage = roomStorage.get(me.getNickname(), partnerName);

        String myNickname = me.getNickname();
        // 이미 존재하는지 확인
        if (roomStorage.get(myNickname, partnerName) != null || roomStorage.get(partnerName, myNickname) != null) {
            log.info("이미존재합니다");
            throw ChatRoomException.CHATROOM_ALREADY_EXIST.get();
        }

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder().member(me).build());
        roomStorage.put(myNickname, partnerName, ChatRoomRequestDTO.builder()
                .chatRoomId(chatRoom.getChatRoomId())
                .memberId(me.getMemberId())
                .nickName(me.getNickname())
                .partnerName(partnerName)
                .createdAt(chatRoom.getCreatedAt())
                .build());
        log.info("roomStorage ={}", roomStorage);

        Member partner = memberRepository.findByNickname(partnerName).orElseThrow(MemberException.NOT_FOUND::get);

        if (partner != null) {
            Long partnerId = partner.getMemberId();

            NotificationSendDTO notificationSendDTO = NotificationSendDTO.builder()
                    .memberId(partnerId)
                    .message("[" + me.getNickname() + "]님이 새로운 채팅방을 개설했습니다.")
                    .notificationType(NotificationType.CHAT)
                    .url("/chat-room/enter/" + chatRoom.getChatRoomId())
                    .build();

            notificationService.send(notificationSendDTO);

            String emitterId = partnerId + "_";
            SseEmitter emitter = sseEmitters.findSingleEmitter(emitterId);

            if (emitter != null) {
                try {
                    emitter.send(new ChatRoomDTO(chatRoom));
                } catch (IOException e) {
                    log.error("Error sending chat room notification to client via SSE: {}", e.getMessage());
                    sseEmitters.delete(emitterId);
                }
            }
        }
        return new ChatRoomDTO(chatRoom);
    }

    // 채팅방 삭제
    public Boolean deleteChatRoom(Long chatRoomId) {
        ChatRoom foundChatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(ChatRoomException.NOT_FOUND::get);

        if (foundChatRoom == null) {
            return false;
        }
        chatRoomRepository.delete(foundChatRoom);
        return true;
    }
}
