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
import java.util.*

@Component("JOIN")
class APIJoinHandler : WebsocketAPIHandler {
    @Autowired
    @Qualifier("UserMap")
    lateinit var users: MutableMap<String, User>

    override fun handle(session: WebSocketSession, msg: ChatMessage) {
        val id = UUID.randomUUID().toString()
        msg.sender = id
        session.sendMessage(TextMessage(Gson().toJson(msg)))
        val newUser = User()
        newUser.session = session
        newUser.id = id
        newUser.lastLoginDate = Calendar.getInstance().time.time
        users.put(id, newUser)
    }
}