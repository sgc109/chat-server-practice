package com.example.chatserverpractice.config

import com.example.chatserverpractice.model.User
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
open class BeanRepository {
    val users: MutableMap<String, User> = HashMap()

    @Bean("UserMap")
    open fun injectUsers() = users
}