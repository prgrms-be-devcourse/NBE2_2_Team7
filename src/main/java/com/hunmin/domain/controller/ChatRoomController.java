package com.hunmin.domain.controller;

import com.hunmin.domain.dto.chat.ChatMessageRequestDTO;
import com.hunmin.domain.dto.chat.ChatRoomDTO;
import com.hunmin.domain.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@Controller
@Log4j2
@RequestMapping("/api/chat-room")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    //채팅방 생성
    @GetMapping
    public String room() {
        return "message/room";
    }

    //채팅방 생성
    @PostMapping("")
    @ResponseBody
    public ChatMessageRequestDTO createRoomByNickName(@RequestParam("nickName") String nickName) {
        return chatRoomService.createChatRoomByNickName(nickName);
    }
    //모든 채팅방 목록 조회
    @GetMapping("/list")
    @ResponseBody
    public List<ChatRoomDTO> rooms() {
        return chatRoomService.findAllRoom();
    }

    //채팅방 안으로 입장
    @GetMapping("/enter/{chatRoomId}")
    public String roomDetail(Model model, @PathVariable String chatRoomId) {
        log.info("room/enter/chatRoomId:{}",chatRoomId);
        model.addAttribute("roomId", Long.valueOf(chatRoomId));
        return "message/roomdetail";
    }
    //단일 채팅방 정보 조회
    @GetMapping("/{chatRoomId}")
    @ResponseBody
    public ChatRoomDTO roomInfo(@PathVariable Long chatRoomId) {
        return chatRoomService.findRoomById(chatRoomId);
    }
    //미완 기술
//    @GetMapping("/user")
//    @ResponseBody
//    public ChatRoomDTO roomInfo1() {
//        return chatRoomService.findRoomById(1L);
//    }
}
