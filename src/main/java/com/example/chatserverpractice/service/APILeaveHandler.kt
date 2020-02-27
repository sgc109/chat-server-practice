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

@Component("LEAVE")
class APILeaveHandler: WebsocketAPIHandler {
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
        val senderId = msg.sender
        val roomId = msg.roomId

        rooms[roomId]?.let {
            for(participant in it) {
                if(participant == senderId) continue
                users[participant]
                        ?.session
                        ?.sendMessage(TextMessage(Gson().toJson(msg)))
                roomRelation.remove(Pair(senderId, participant))
                roomRelation.remove(Pair(participant, senderId))
            }
        }

        users[senderId]?.roomIds?.remove(roomId)
        rooms.remove(roomId)
    }
}