package com.example.gblocalchat.Server;

import com.example.gblocalchat.Command;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChatServer {

    private static final int PORT = 8189;
    private final AuthService authService;
    private final Map<String, ClientHandler> clients;
    public static final Logger LOGGER = LogManager.getLogger(ChatServer.class);

    public ChatServer() {
        clients = new HashMap<>();
        authService = new InMemoryAuthService();
    }


    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            LOGGER.info("Сервер запущен!");
            while (true) {
                final Socket socket = serverSocket.accept();
                new ClientHandler(socket, this);
                LOGGER.info("Клиент успешно подключился!");
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
        broadcastClientList();
    }

    public void unSubscribe(ClientHandler client) {
        clients.remove(client.getNick());
        broadcastClientList();
    }

    public void broadcast(String message) {
        for (ClientHandler client : clients.values()) {
            client.sendMessage(message);
        }
    }

    public void sendMessageToClient(ClientHandler from, String nickTo, String message){
        final ClientHandler clientTo = clients.get(nickTo);
        if(clientTo != null) {
            clientTo.sendMessage("От " + from.getNick() + ": " + message);
            from.sendMessage("Участнику " + nickTo + ": " + message);
            return;
        }
        from.sendMessage("Участника с ником " + nickTo + " нет в чате");
    }

    public void broadcastClientList(){
        final String message = clients.values().stream()
                .map(ClientHandler::getNick)
                .collect(Collectors.joining(" "));
        broadcast(Command.CLIENTS, message);
    }

    private void broadcast(Command command, String message) {
        for (ClientHandler client : clients.values()) {
            client.sendMessage(command, message);
        }
    }
}
