package com.hunmin.domain.controller;

import com.hunmin.domain.dto.chat.ChatMessageRequestDTO;
import com.hunmin.domain.dto.chat.ChatRoomDTO;
import com.hunmin.domain.entity.ChatRoom;
import com.hunmin.domain.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
@RequiredArgsConstructor
@Controller
@Log4j2
@RequestMapping("/api/chat-room")
@Tag(name = "채팅목록", description = "채팅목록 CRUD")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    //채팅방 생성
    @GetMapping
    public String room() {
        return "message/room";
    }

    //채팅방 생성
    @PostMapping("/{nickName}/{currentMemberEmail}")
    @ResponseBody
    @Operation(summary = "채팅방 생성", description = "채팅방을 이름으로 생성하는 API")
    public ChatMessageRequestDTO createRoomByNickName(@PathVariable("nickName") String nickName
            ,@PathVariable("currentMemberEmail") String currentMemberEmail) {
        log.info("nickName={}", nickName);
        log.info("Email={}", currentMemberEmail);
//        String currentMemberEmail = authentication.getName(); -> 토큰 처리 후 주석풀기
        return chatRoomService.createChatRoomByNickName(nickName, currentMemberEmail);
    }

    //나랑 관련된 채팅방만 조회
    @GetMapping("/list")
    @ResponseBody
    public List<ChatRoomDTO> myRooms(@RequestParam("email") String email) {
        log.info("email={}",email);
//        String currentMemberEmail = authentication.getName(); -> 토큰 처리 후 주석풀기
        return chatRoomService.findRoomByEmail(email);
    }

    //채팅방 안으로 입장
    @GetMapping("/enter/{chatRoomId}")
    public String RoomEnter(Model model, @PathVariable String chatRoomId) {
        log.info("room/enter/chatRoomId:{}",chatRoomId);
        model.addAttribute("roomId", Long.valueOf(chatRoomId));
        return "message/roomdetail";
    }
    //단일 채팅방 정보 조회
    @GetMapping("/{chatRoomId}")
    @ResponseBody
    @Operation(summary = "단일 채팅방 정보 조회", description = "검색하고 싶은 채팅방을 조회하는 API")
    public ChatRoomDTO roomInfo(@PathVariable Long chatRoomId) {
        return chatRoomService.findRoomById(chatRoomId);
    }
    //채팅방 삭제
    @DeleteMapping("/{chatRoomId}")
    @ResponseBody
    @Operation(summary = "채팅방 삭제", description = "삭제하고 싶은 채팅방을 삭제하는 API")
    public Boolean deleteRoom(@PathVariable Long chatRoomId) {
        return chatRoomService.deleteChatRoom(chatRoomId);
    }
}
