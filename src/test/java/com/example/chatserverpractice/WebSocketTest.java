package com.example.chatserverpractice;

import com.example.chatserverpractice.model.ChatMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class WebSocketTest {
    static final String SERVER_URL = "ws://localhost:8080";
    static final String WEB_SOCKET_ENDPOINT_URI = SERVER_URL + "/ws-stomp";
    static final String WEB_SOCKET_PUB_TOPIC = "/pub/chat/message";
    static final String WEB_SOCKET_SUB_TOPIC = "/sub/chat/room/1";

    WebSocketStompClient stompClient;
    BlockingQueue<ChatMessage> blockingQueue;
    ChatMessage sampleMessage;

    @BeforeEach
    void setUp() {
        stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        blockingQueue = new LinkedBlockingQueue<>();

        sampleMessage = new ChatMessage();
        sampleMessage.setMessage("Hello world!");
        sampleMessage.setRoomId("roomId");
        sampleMessage.setSender("sender");
        sampleMessage.setType(ChatMessage.MessageType.TALK);
    }

    @Test
    void shouldGetMessageFromServer() throws Exception {
        StompSession session = stompClient
                .connect(WEB_SOCKET_ENDPOINT_URI, new StompSessionHandlerAdapter() {
                })
                .get(1, TimeUnit.SECONDS);

        session.subscribe(WEB_SOCKET_SUB_TOPIC, new DefaultStompFrameHandler());

        session.send(WEB_SOCKET_PUB_TOPIC, sampleMessage);

        Assertions.assertEquals(sampleMessage, blockingQueue.poll(1, TimeUnit.SECONDS));
    }

    class DefaultStompFrameHandler implements StompFrameHandler {

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return null;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            blockingQueue.offer((ChatMessage)payload);
        }
    }
}
