package com.example.chatserverpractice.service

import com.example.chatserverpractice.WebsocketAPIHandler
import com.example.chatserverpractice.model.ChatMessage
import com.example.chatserverpractice.model.User
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

@Component("SEND")
class APISendHandler: WebsocketAPIHandler {
    @Autowired
    @Qualifier("UserMap")
    private lateinit var users: MutableMap<String, User>

    override fun handle(session: WebSocketSession, msg: ChatMessage) {
        users[msg.receiver]
                ?.session
                ?.sendMessage(TextMessage(Gson().toJson(msg)))

    }
}