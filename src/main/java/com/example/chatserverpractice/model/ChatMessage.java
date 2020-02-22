package com.example.chatserverpractice.model;

import lombok.Data;

@Data
public class ChatMessage {
    public enum MessageType {
        JOIN, SEND, LEAVE, BROADCAST
    }
    private MessageType type;
    private String receiver;
    private String sender;
    private String content;
}
