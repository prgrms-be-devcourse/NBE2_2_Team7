package com.hunmin.domain.controller;

import com.hunmin.domain.dto.chat.ChatMessageRequestDTO;
import com.hunmin.domain.dto.chat.ChatRoomDTO;
import com.hunmin.domain.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "채팅목록", description = "채팅목록 CRUD")
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
    @Operation(summary = "채팅방 생성", description = "채팅방을 이름으로 생성하는 API")
    public ChatMessageRequestDTO createRoomByNickName(@RequestParam("nickName") String nickName) {
        return chatRoomService.createChatRoomByNickName(nickName);
    }
    //모든 채팅방 목록 조회
    @GetMapping("/list")
    @ResponseBody
    @Operation(summary = "채팅 목록 조회", description = "모든 채팅방 목록을 조회하는 API")
    public List<ChatRoomDTO> rooms() {
        return chatRoomService.findAllRoom();
    }
//    //나랑 관련된 채팅방만 조회
//    @GetMapping("/list")
//    @ResponseBody
//    public List<ChatRoomDTO> myRooms(Authentication authentication) {
//        String currentMemberEmail = authentication.getName();
//        log.info("current Email {}",currentMemberEmail);
//        return chatRoomService.findRoomByEmail(currentMemberEmail);
//    }

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
    @Operation(summary = "단일 채팅방 정보 조회", description = "검색하고 싶은 채팅방을 조회하는 API")
    public ChatRoomDTO roomInfo(@PathVariable Long chatRoomId) {
        return chatRoomService.findRoomById(chatRoomId);
    }

}
