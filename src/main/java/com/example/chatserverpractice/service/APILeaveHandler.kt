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

@Component("LEAVE")
class APILeaveHandler: WebsocketAPIHandler {
    @Autowired
    @Qualifier("UserMap")
    private lateinit var users: MutableMap<String, User>

    override fun handle(session: WebSocketSession, msg: ChatMessage) {
        val senderId = msg.sender
        val receiverId = msg.receiver

        users[senderId]?.userTalkingWith?.remove(receiverId)
        users[receiverId]?.userTalkingWith?.remove(senderId)

        users[receiverId]
                ?.session
                ?.sendMessage(TextMessage(Gson().toJson(msg)))

    }
}