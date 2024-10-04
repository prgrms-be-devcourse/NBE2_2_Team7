package com.hunmin.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hunmin.domain.dto.chat.ChatMessageRequestDTO;
import com.hunmin.domain.dto.chat.ChatRoomDTO;
import com.hunmin.domain.entity.ChatRoom;
import com.hunmin.domain.entity.Member;
import com.hunmin.domain.exception.ChatRoomException;
import com.hunmin.domain.repository.ChatRoomRepository;
import com.hunmin.domain.repository.MemberRepository;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatRoomService {

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, ChatRoom> roomStorage;
    private final ObjectMapper objectMapper;

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    // 단일 채팅방 조회
    public ChatRoomDTO findRoomById(Long id) {
        ChatRoom chatRoom = chatRoomRepository.findById(id).orElseThrow(ChatRoomException.NOT_FOUND::get);
        return new ChatRoomDTO(chatRoom);
    }

    // 나랑 관련된 모든 채팅방 조회
    public List<ChatRoomDTO> findRoomByEmail(String email) {
        Member me = memberRepository.findByEmail(email);
        List<ChatRoom> partnerNameAndChatRoom = roomStorage.values(me.getNickname());
        log.info("partnerNameAndChatRoom={}", partnerNameAndChatRoom);
        List<ChatRoomDTO> chatRoomDTOList = new ArrayList<>();
        for (Object rawChatRoom : partnerNameAndChatRoom) {
            ChatRoom chatRoom;
            if (rawChatRoom instanceof LinkedHashMap) {
                chatRoom = objectMapper.convertValue(rawChatRoom, ChatRoom.class);
            } else {
                chatRoom = (ChatRoom) rawChatRoom;
            }
            chatRoomDTOList.add(new ChatRoomDTO(chatRoom));
        }

        Set<String> partnerNames = roomStorage.keys(me.getNickname());
        for (String partnerName : partnerNames) {
            log.info("partnerName={}", partnerName);
            Object rawChatRoom = roomStorage.get(partnerName, me.getNickname());
            if (rawChatRoom != null) {
                ChatRoom chatRoom;
                log.info("chatRoom={}", rawChatRoom);
                if (rawChatRoom instanceof LinkedHashMap) {
                    chatRoom = objectMapper.convertValue(rawChatRoom, ChatRoom.class);
                } else {
                    chatRoom = (ChatRoom) rawChatRoom;
                    log.info("chatRoom={}", chatRoom);
                }
                chatRoomDTOList.add(new ChatRoomDTO(chatRoom));
            }
        }
        return chatRoomDTOList;
    }

    // 채팅방 생성 : 이름으로 상대방 검색 후 채팅방 개설  -> 새로운
    public ChatMessageRequestDTO createChatRoomByNickName(String partnerName, String myEmail) {
        Member me = memberRepository.findByEmail(myEmail);
        log.info("me ={}", me);
        ChatRoom chatRoomFromStorage = roomStorage.get(me.getNickname(), partnerName);

        String myNickname = me.getNickname();
        // 이미 존재하는지 확인
        if (roomStorage.get(myNickname, partnerName) != null || roomStorage.get(partnerName, myNickname) != null) {
            log.info("이미존재합니다");
            throw ChatRoomException.CHATROOM_ALREADY_EXIST.get();
        }

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder().member(me).build());
        roomStorage.put(myNickname, partnerName, chatRoom);
        log.info("roomStorage ={}", roomStorage);

        return ChatMessageRequestDTO.builder()
                .chatRoomId(chatRoom.getChatRoomId())
                .userCount(chatRoom.getUserCount())
                .createdAt(chatRoom.getCreatedAt())
                .nickName(me.getNickname())
                .MemberId(me.getMemberId())
                .build();
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
