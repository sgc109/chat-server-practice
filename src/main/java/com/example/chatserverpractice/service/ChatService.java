package com.example.chatserverpractice.service;

import com.example.chatserverpractice.Router;
import com.example.chatserverpractice.model.ChatMessage;
import com.example.chatserverpractice.model.User;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

@Service
public class ChatService {
    @Autowired
    @Qualifier("UserMap")
    private Map<String, User> users;

    @Autowired
    private Router router;

    public void handleMessage(WebSocketSession session, ChatMessage msg) throws Exception {
        switch (msg.getType()) {
            case JOIN:
            case BROADCAST:
            case SEND:
            case LEAVE:
                router.route(msg.getType().name(), session, msg);
                break;
        }
    }
}
