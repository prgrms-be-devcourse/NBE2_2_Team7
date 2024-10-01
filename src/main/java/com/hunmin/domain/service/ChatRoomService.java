package com.hunmin.domain.service;

import com.hunmin.domain.dto.chat.ChatMessageDTO;
import com.hunmin.domain.dto.chat.ChatMessageRequestDTO;
import com.hunmin.domain.dto.chat.ChatRoomDTO;
import com.hunmin.domain.entity.ChatRoom;
import com.hunmin.domain.entity.Member;
import com.hunmin.domain.exception.exception.ChatRoomException;
import com.hunmin.domain.repository.ChatRoomRepository;
import com.hunmin.domain.repository.MemberRepository;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatRoomService {
    private static final String CHAT_ROOMS = "CHAT_ROOM"; // 채팅룸 저장
    public static final String USER_COUNT = "USER_COUNT"; // 채팅룸에 입장한 클라이언트수 저장
    public static final String ENTER_INFO = "ENTER_INFO"; // 채팅룸에 입장한 클라이언트의 sessionId와 채팅룸 id를 맵핑한 정보 저장

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, ChatRoom> hashOpsChatRoom;
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, Long> hashOpsEnterInfo;
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOps;

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    // 모든 채팅방 조회
    public List<ChatRoomDTO> findAllRoom() {
        List<ChatRoomDTO> chatRoomDTOList = new ArrayList<>();
        List<ChatRoom> chatRoomList = chatRoomRepository.findAllOrderByLatestMessageDateDesc();
        for (ChatRoom chatRoom : chatRoomList) {
            chatRoomDTOList.add(new ChatRoomDTO(chatRoom));
        }
        log.info("chatRoomDTOList: {}", chatRoomDTOList);
        return chatRoomDTOList;
    }

    // 특정 채팅방 조회
    public ChatRoomDTO findRoomById(Long id) {
        ChatRoom chatRoom = chatRoomRepository.findById(id).orElseThrow(ChatRoomException.NOT_FOUND::get);
        return new ChatRoomDTO(chatRoom);
    }

    // 채팅방 생성 : 서버간 채팅방 공유를 위해 DB에 저장한다.
    public ChatRoomDTO createChatRoom(String memberId) {
        Member member = memberRepository.findById(Long.parseLong(memberId))
                .orElseThrow(ChatRoomException.NOT_FOUND::get);
        ChatRoom chatRoom = ChatRoom.builder().member(member).build();
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        return new ChatRoomDTO(savedChatRoom);
    }

    // 채팅방 생성 : 이름으로 상대방 검색 후 채팅방 개설
    public ChatMessageRequestDTO createChatRoomByNickName(String nickName) {
        Member member = memberRepository.findByNickname(nickName)
                .orElseThrow(() -> new RuntimeException("없는 사용자 불러오기"));
        ChatRoom chatRoom = ChatRoom.builder().member(member).userCount(1).build();
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        return ChatMessageRequestDTO.builder()
                .chatRoomId(savedChatRoom.getChatRoomId())
                .userCount(savedChatRoom.getUserCount())
                .createdAt(savedChatRoom.getCreatedAt())
                .nickName(member.getNickname())
                .MemberId(member.getMemberId()).build();
    }
    // 채팅방 삭제
    public void deleteChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(ChatRoomException.NOT_FOUND::get);
        chatRoomRepository.delete(chatRoom);
    }

    // 유저가 입장한 채팅방ID와 유저 세션ID 맵핑 정보 저장
    public void setUserEnterInfo(String sessionId, Long roomId) {
        hashOpsEnterInfo.put(ENTER_INFO, sessionId, roomId);
    }

    // 유저 세션으로 입장해 있는 채팅방 ID 조회
    public Long getUserEnterRoomId(String sessionId) {
        return hashOpsEnterInfo.get(ENTER_INFO, sessionId);
    }

    // 유저 세션정보와 맵핑된 채팅방ID 삭제
    public void removeUserEnterInfo(String sessionId) {
        Long roomId = hashOpsEnterInfo.get(ENTER_INFO, sessionId);
        if (roomId == null) {
            throw new RuntimeException("세션 조회중 roomId가 없습니다.");
        }
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(ChatRoomException.NOT_FOUND::get);
        chatRoomRepository.delete(chatRoom);
    }

    // 채팅방 유저수 조회
    public long getUserCount(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(ChatRoomException.NOT_FOUND::get);
        return chatRoom.getUserCount();
    }

    // 채팅방에 입장한 유저수 +1
    public long plusUserCount(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(ChatRoomException.NOT_FOUND::get);
        chatRoom.setUserCount(chatRoom.getUserCount() + 1);
        chatRoomRepository.save(chatRoom);
        return chatRoom.getUserCount();
    }

    // 채팅방에 입장한 유저수 -1
    public long minusUserCount(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(ChatRoomException.NOT_FOUND::get);
        chatRoom.setUserCount(chatRoom.getUserCount() - 1);
        chatRoomRepository.save(chatRoom);
        if (chatRoom.getUserCount() < 1) {
            chatRoomRepository.delete(chatRoom);
            return 0;
        }
        return chatRoom.getUserCount();
    }
}
