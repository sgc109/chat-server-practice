package com.example.chatserverpractice

import com.example.chatserverpractice.model.ChatMessage
import org.springframework.web.socket.WebSocketSession

interface WebsocketAPIHandler {
    fun handle(session: WebSocketSession, msg: ChatMessage): Unit
}