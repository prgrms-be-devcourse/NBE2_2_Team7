package com.hunmin.domain.controller.websocket;

import com.hunmin.domain.dto.chat.ChatMessageRequestDTO;
import com.hunmin.domain.dto.chat.ChatRoomDTO;
import com.hunmin.domain.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@Controller
@Log4j2
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/room")
    public String room() {
        return "message/room";
    }

    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoomDTO> rooms() {
        return chatRoomService.findAllRoom();
    }

    @PostMapping("/room")
    @ResponseBody
    public ChatRoomDTO createRoom(@RequestParam("memberId") String memberId) {
        return chatRoomService.createChatRoom(memberId);
    }
    @PostMapping("/room2")
    @ResponseBody
    public ChatMessageRequestDTO createRoomByNickName(@RequestParam("nickName") String nickName) {
        return chatRoomService.createChatRoomByNickName(nickName);
    }

    @GetMapping("/room/enter/{chatRoomId}")
    public String roomDetail(Model model, @PathVariable String chatRoomId) {
        log.info("room/enter/chatRoomId:{}",chatRoomId);
        model.addAttribute("roomId", Long.valueOf(chatRoomId));
        return "message/roomdetail";
    }

    @GetMapping("/room/{chatRoomId}")//sub으로 문자 수신
    @ResponseBody
    public ChatRoomDTO roomInfo(@PathVariable Long chatRoomId) {
        return chatRoomService.findRoomById(chatRoomId);
    }
    @GetMapping("/user")
    @ResponseBody
    public ChatRoomDTO roomInfo1() {
        return chatRoomService.findRoomById(1L);
    }
    // 채팅방 삭제 엔드포인트
    @DeleteMapping("/room/{chatRoomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long chatRoomId) {
        chatRoomService.deleteChatRoom(chatRoomId);
        return ResponseEntity.noContent().build();
    }
}