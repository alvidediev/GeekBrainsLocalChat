package com.example.gblocalchat.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {

    private static final int PORT = 8189;
    private final AuthService authService;
    private final Map<String, ClientHandler> clients;

    public ChatServer() {
        clients = new HashMap<>();
        authService = new InMemoryAuthService();
    }


    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                System.out.println("Ждем подключения клиента");
                final Socket socket = serverSocket.accept();
                new ClientHandler(socket, this);
                System.out.println("Клиент подключился");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AuthService getAuthService() {
        return authService;
    }

    public boolean isNickBusy(String nick) {
        return clients.containsKey(nick);
    }

    public void subscribe(ClientHandler client) {
        clients.put(client.getNick(), client);
    }

    public void unSubscribe(ClientHandler client) {
        clients.remove(client.getNick());
    }

    public void broadcast(String message) {
        for (ClientHandler client : clients.values()) {
            client.sendMessage(message);
        }
    }
}
