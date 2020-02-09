package com.example.chatserverpractice.controller;

import com.example.chatserverpractice.repository.ChatRoomRepository;
import com.example.chatserverpractice.model.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/room")
    public String rooms(Model model) {
        return "/chat/room";
    }

    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);
        return "/chat/roomdetail";
    }

    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoom> findAllRoom() {
        return chatRoomRepository.findAllRooms();
    }

    @PostMapping("/rooms")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam String name) {
        return chatRoomRepository.createChatRoom(name);
    }

    @GetMapping("/rooms/{roomId}")
    @ResponseBody
    public ChatRoom findRoomById(@PathVariable String roomId) {
        return chatRoomRepository.findRoomById(roomId);
    }
}
