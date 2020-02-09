package com.example.chatserverpractice.model;

import lombok.Data;

@Data
public class ChatMessage {
    public enum MessageType {
        JOIN, TALK
    }
    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
}
