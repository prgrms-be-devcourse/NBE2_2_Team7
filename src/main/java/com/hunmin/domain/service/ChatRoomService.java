package com.hunmin.domain.service;

import com.hunmin.domain.dto.chat.ChatMessageRequestDTO;
import com.hunmin.domain.dto.chat.ChatRoomDTO;
import com.hunmin.domain.entity.ChatRoom;
import com.hunmin.domain.entity.Member;
import com.hunmin.domain.exception.ChatRoomException;
import com.hunmin.domain.exception.MemberException;
import com.hunmin.domain.repository.ChatRoomRepository;
import com.hunmin.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatRoomService {

    public static Map<String, String> roomStorage = new ConcurrentHashMap<>();

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    // 단일 채팅방 조회
    public ChatRoomDTO findRoomById(Long id) {
        ChatRoom chatRoom = chatRoomRepository.findById(id).orElseThrow(ChatRoomException.NOT_FOUND::get);
        return new ChatRoomDTO(chatRoom);
    }

    // 이메일로 채팅방 조회
    public List<ChatRoomDTO> findRoomByEmail(String email) {
        Member member = memberRepository.findByEmail(email);
        List<ChatRoom> chatRoom = chatRoomRepository.findByMemberId(member.getMemberId()).orElseThrow(ChatRoomException.NOT_FOUND::get);
        return chatRoom.stream().map(ChatRoomDTO::new).toList();
    }

    // 모든 채팅방 조회
    public List<ChatRoomDTO> findAllRoom() {
        List<ChatRoomDTO> chatRoomDTOList = new ArrayList<>();
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        for (ChatRoom chatRoom : chatRoomList) {
            chatRoomDTOList.add(new ChatRoomDTO(chatRoom));
        }
        log.info("chatRoomDTOList: {}", chatRoomDTOList);
        return chatRoomDTOList;
    }

    // 채팅방 생성 : 이름으로 상대방 검색 후 채팅방 개설
    public ChatMessageRequestDTO createChatRoomByNickName(String partnerName,String myEmail) {
        Member me = memberRepository.findByEmail(myEmail);

        if ((roomStorage.get(me.getNickname())!=null && roomStorage.get(me.getNickname()).equals(partnerName))
        ||){
            throw ChatRoomException.CHATROOM_ALREADY_EXIST.get();
        }
        roomStorage.put(partnerName,me.getNickname());

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder().member(me).build());

        return ChatMessageRequestDTO.builder()
                .chatRoomId(chatRoom.getChatRoomId())
                .userCount(chatRoom.getUserCount())
                .createdAt(chatRoom.getCreatedAt())
                .nickName(me.getNickname())
                .MemberId(me.getMemberId()).build();
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

    // 채팅방 유저수 조회
    public long getUserCount(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(Long.parseLong(roomId)).orElseThrow(ChatRoomException.NOT_FOUND::get);
        return chatRoom.getUserCount();
    }

    // 채팅방에 입장한 유저수 +1
    public long plusUserCount(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(Long.parseLong(roomId)).orElseThrow(ChatRoomException.NOT_FOUND::get);
        chatRoom.setUserCount(chatRoom.getUserCount() + 1);
        chatRoomRepository.save(chatRoom);
        return chatRoom.getUserCount();
    }

    // 채팅방에 입장한 유저수 -1
    public long minusUserCount(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(Long.parseLong(roomId)).orElseThrow(ChatRoomException.NOT_FOUND::get);
        chatRoom.setUserCount(chatRoom.getUserCount() - 1);
        chatRoomRepository.save(chatRoom);
        if (chatRoom.getUserCount() < 1) {
            chatRoomRepository.delete(chatRoom);
            return 0;
        }
        return chatRoom.getUserCount();
    }
}
