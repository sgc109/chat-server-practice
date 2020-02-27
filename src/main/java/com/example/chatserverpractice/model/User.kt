package com.example.chatserverpractice.model;

import org.springframework.web.socket.WebSocketSession
import java.util.*

data class User (
    var id: String = "",
    var lastLoginDate: Long = 0,
    var roomIds: MutableSet<String> = HashSet(),
    var session: WebSocketSession? = null
)