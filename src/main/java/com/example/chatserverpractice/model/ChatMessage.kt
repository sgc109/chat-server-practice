package com.example.chatserverpractice.model;

data class ChatMessage(
    var type: MessageType,
    var receiver: String,
    var sender: String,
    var content: String
) {
    enum class MessageType {
        JOIN, SEND, LEAVE, BROADCAST
    }
}