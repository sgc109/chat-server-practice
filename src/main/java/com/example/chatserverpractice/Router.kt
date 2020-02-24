package com.example.chatserverpractice

import com.example.chatserverpractice.model.ChatMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
class Router {
    @Autowired
    private lateinit var handlers: Map<String, WebsocketAPIHandler>

    fun route(API: String, session: WebSocketSession, msg: ChatMessage){
        handlers[API]?.handle(session, msg)
    }
}