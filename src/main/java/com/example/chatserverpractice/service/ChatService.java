package com.example.chatserverpractice.service;

import com.example.chatserverpractice.Router;
import com.example.chatserverpractice.model.ChatMessage;
import com.example.chatserverpractice.model.User;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

@Service
public class ChatService {
    @Autowired
    @Qualifier("UserMap")
    private Map<String, User> users;

    @Autowired
    private Router router;

    public void handleMessage(WebSocketSession session, ChatMessage msg) throws Exception {
        switch (msg.getType()) {
            case JOIN:
//                String id = UUID.randomUUID().toString();
//                msg.setReceiver(id);
//                session.sendMessage(new TextMessage(new Gson().toJson(msg)));
//                User newUser = new User();
//                newUser.setSession(session);
//                newUser.setId(id);
//                newUser.setLastLoginDate(Calendar.getInstance().getTime().getTime());
//                users.put(id, newUser);
                router.route("JOIN", session, msg);
                break;
            case BROADCAST:
                User me = users.get(msg.getSender());
                for (User user : users.values()) {
                    if (!me.getId().equals(user.getId()) && !me.getUserTalkingWith().contains(user.getId())) {
                        user.getSession()
                                .sendMessage(new TextMessage(new Gson().toJson(msg)));
                    }
                }
                break;
            case SEND:
                users.get(msg.getReceiver())
                        .getSession()
                        .sendMessage(new TextMessage(new Gson().toJson(msg)));
                break;
            case LEAVE:
                String senderId = msg.getSender();
                String receiverId = msg.getReceiver();

                users.get(senderId).getUserTalkingWith().remove(receiverId);
                users.get(receiverId).getUserTalkingWith().remove(senderId);

                users.get(receiverId)
                        .getSession()
                        .sendMessage(new TextMessage(new Gson().toJson(msg)));
                break;
        }
    }
}
