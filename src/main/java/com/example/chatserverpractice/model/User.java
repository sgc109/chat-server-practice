package com.example.chatserverpractice.model;

import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    String id;
    Long lastLoginDate;
    Set<String> userTalkingWith = new HashSet<>();
    WebSocketSession session;
}
