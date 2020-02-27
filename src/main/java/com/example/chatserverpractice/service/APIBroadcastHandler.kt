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
import java.util.*

@Component("BROADCAST")
class APIBroadcastHandler : WebsocketAPIHandler {
    @Autowired
    @Qualifier("UserMap")
    private lateinit var users: MutableMap<String, User>

    @Autowired
    @Qualifier("RoomRelation")
    private lateinit var roomRelation: MutableMap<Pair<String, String>, String>

    @Autowired
    @Qualifier("RoomMap")
    private lateinit var rooms: MutableMap<String, List<String>>

    override fun handle(session: WebSocketSession, msg: ChatMessage) {
        if(users.containsKey(msg.sender)) {
            val (id) = users[msg.sender]!!
            for ((id1, _, _, session1) in users.values) {
                if (id != id1 && !roomRelation.containsKey(Pair(id, id1))) {
                    session1
                            ?.sendMessage(TextMessage(Gson().toJson(msg)))
                    val roomId = UUID.randomUUID().toString()
                    rooms[roomId] = listOf(id, id1)
                    users[id]?.roomIds?.add(roomId)
                    users[id1]?.roomIds?.add(roomId)
                }
            }
        }
    }
}