package com.example.chatserverpractice;

import com.example.chatserverpractice.model.ChatMessage;
import com.example.chatserverpractice.service.ChatService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
public class SocketHandler extends TextWebSocketHandler {
    private final ChatService chatService;


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        chatService.handleMessage(session, new Gson().fromJson(message.getPayload(), ChatMessage.class));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        System.out.println("WebSocket client connected!!!!!!!!!!!");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }
}
