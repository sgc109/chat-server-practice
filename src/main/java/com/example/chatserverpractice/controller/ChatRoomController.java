package com.example.chatserverpractice.controller;

import com.example.chatserverpractice.ChatRoomRepository;
import com.example.chatserverpractice.model.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/rooms")
    public List<ChatRoom> findAllRoom() {
        return chatRoomRepository.findAllRooms();
    }

    @PostMapping("/rooms")
    public ChatRoom createRoom(@RequestParam String name) {
        return chatRoomRepository.createChatRoom(name);
    }

    @GetMapping("/rooms/{roomId}")
    public ChatRoom findRoomById(@PathVariable String roomId) {
        return chatRoomRepository.findRoomByid(roomId);
    }
}
