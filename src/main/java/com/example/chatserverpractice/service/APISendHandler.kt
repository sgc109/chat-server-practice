package com.example.chatserverpractice.service

import com.example.chatserverpractice.WebsocketAPIHandler
import com.example.chatserverpractice.model.ChatMessage
import com.example.chatserverpractice.model.User
import com.google.gson.Gson
import javafx.util.Pair
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

    @Autowired
    @Qualifier("RoomMap")
    private lateinit var rooms: MutableMap<String, List<String>>

    @Autowired
    @Qualifier("RoomRelation")
    private lateinit var roomRelation: MutableMap<Pair<String, String>, String>

    override fun handle(session: WebSocketSession, msg: ChatMessage) {
        rooms[msg.roomId]?.let {
            for(userId in it) {
                if(userId == msg.sender) continue
                if(roomRelation.containsKey(Pair(msg.sender, userId))) {
                    roomRelation[Pair(msg.sender, userId)] = msg.roomId
                    roomRelation[Pair(userId, msg.sender)] = msg.roomId
                }

                users[userId]?.session?.sendMessage(TextMessage(Gson().toJson(msg)))
            }
        }
    }
}