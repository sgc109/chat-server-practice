package com.example.chatserverpractice;

import com.example.chatserverpractice.model.ChatMessage;
import com.example.chatserverpractice.model.User;
import com.google.gson.Gson;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BroadcastSendLeaveIntegrationTest {
    static final String SERVER_URL = "ws://localhost:8080";
    static final String WEB_SOCKET_ENDPOINT_URI = SERVER_URL + "/ws";
    static Boolean isConnected = true;

    static User user1;
    static User user2;

    static WebSocketListener webSocketListenerForUser1;
    static WebSocketListener webSocketListenerForUser2;

    static OkHttpClient okHttpClientForUser1;
    static OkHttpClient okHttpClientForUser2;
    static WebSocket webSocketOfUser1;
    static WebSocket webSocketOfUser2;

    static ChatMessage msgForJoin;
    static ChatMessage msgForSendFromUser2;
    static ChatMessage msgForBroadcastFromUser1;
    static ChatMessage msgForLeaveFromUser1;

    static boolean pingPongDone;

    @BeforeAll
    static void init() {
        okHttpClientForUser1 = new OkHttpClient();
        okHttpClientForUser2 = new OkHttpClient();
        Request request = new Request.Builder()
                .url(WEB_SOCKET_ENDPOINT_URI)
                .build();

        webSocketListenerForUser1 = new WebSocketListenerForUser1();
        webSocketListenerForUser2 = new WebSocketListenerForUser2();

        webSocketOfUser1 = okHttpClientForUser1.newWebSocket(request, webSocketListenerForUser1);
        webSocketOfUser2 = okHttpClientForUser2.newWebSocket(request, webSocketListenerForUser2);

        msgForJoin = new ChatMessage();
        msgForJoin.setType(ChatMessage.MessageType.JOIN);

        pingPongDone = false;

        user1 = new User();
        user1.setLastLoginDate(Calendar.getInstance().getTime().getTime());

        user2 = new User();
        user2.setLastLoginDate(Calendar.getInstance().getTime().getTime());
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    @Order(0)
    void shouldConnectToWebSocketServer() {
        Assertions.assertEquals(isConnected, true);
    }

    @Test
    @Order(1)
    void shouldJoinSuccessfully() throws Exception {
        webSocketOfUser1.send(new Gson().toJson(msgForJoin));
        webSocketOfUser2.send(new Gson().toJson(msgForJoin));
        Thread.sleep(3000);
    }

    @Test
    @Order(2)
    void shouldBroadcastSendAndLeaveBetween2Users() {
        msgForBroadcastFromUser1 = new ChatMessage();
        msgForBroadcastFromUser1.setType(ChatMessage.MessageType.BROADCAST);
        msgForBroadcastFromUser1.setSender(user1.getId());
        msgForBroadcastFromUser1.setContent("Hello world!");

        msgForSendFromUser2 = new ChatMessage();
        msgForSendFromUser2.setType(ChatMessage.MessageType.SEND);
        msgForSendFromUser2.setSender(user2.getId());
        msgForSendFromUser2.setReceiver(user1.getId());
        msgForSendFromUser2.setContent("Oh Hi there!");

        msgForLeaveFromUser1 = new ChatMessage();
        msgForLeaveFromUser1.setType(ChatMessage.MessageType.LEAVE);
        msgForLeaveFromUser1.setSender(user1.getId());
        msgForLeaveFromUser1.setReceiver(user2.getId());

        webSocketOfUser1.send(new Gson().toJson(msgForBroadcastFromUser1));
    }

    static class WebSocketListenerForUser1 extends WebSocketListener {
        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
            super.onMessage(webSocket, text);
            ChatMessage message = new Gson().fromJson(text, ChatMessage.class);
            switch (message.getType()) {
                case JOIN:
                    System.out.println("Join!!!!");
                    user1.setId(message.getReceiver());
                    break;
                case BROADCAST:
                    System.out.println("Broadcast " + message.getSender() + " -> user1");
                    break;
                case SEND:
                    System.out.println("Send " + message.getSender() + " -> user1");

                    webSocket.send(new Gson().toJson(msgForLeaveFromUser1));
                    break;
                case LEAVE:
                    System.out.println("Leave " + message.getSender() + " -> user1");
                    break;
                default:
                    throw new RuntimeException("Not supported message type! " + message.getType().toString());
            }
        }

        @Override
        public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
            super.onFailure(webSocket, t, response);
            isConnected = false;
            throw new RuntimeException(t);
        }
    }

    static class WebSocketListenerForUser2 extends WebSocketListener {

        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
            super.onMessage(webSocket, text);
            ChatMessage message = new Gson().fromJson(text, ChatMessage.class);
            switch (message.getType()) {
                case JOIN:
                    System.out.println("Join!!!!");
                    user2.setId(message.getReceiver());
                    break;
                case BROADCAST:
                    System.out.println("Broadcast " + message.getSender() + " -> user2");

                    webSocket.send(new Gson().toJson(msgForSendFromUser2));
                    break;
                case SEND:
                    System.out.println("Send " + message.getSender() + " -> user2");
                    break;
                case LEAVE:
                    System.out.println("Leave " + message.getSender() + " -> user2");

                    pingPongDone = true;
                    break;
                default:
                    throw new RuntimeException("Not supported message type! " + message.getType().toString());
            }
        }

        @Override
        public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
            super.onFailure(webSocket, t, response);
            isConnected = false;
            throw new RuntimeException(t);
        }
    }
}
