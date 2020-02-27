package com.example.chatserverpractice.config

import com.example.chatserverpractice.model.User
import javafx.util.Pair
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
open class BeanRepository {
    val users: MutableMap<String, User> = HashMap()
    val rooms: MutableMap<String, List<String>> = HashMap()
    val roomRelation: MutableMap<Pair<String, String>, String> = HashMap()

    @Bean("UserMap")
    open fun injectUsers() = users
    @Bean("RoomMap")
    open fun injectRooms() = rooms
    @Bean("RoomRelation")
    open fun injectRoomRelation() = roomRelation
}