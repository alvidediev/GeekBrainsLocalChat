package com.example.gblocalchat.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    //констарнты сделанные мной...но почему то с ними не работали личные сообщение
    //private static final String COMMAND_PREFIX = "/";
    //private static final String END = COMMAND_PREFIX + "end";
    // private static final String PRIVATE_MESSAGE = COMMAND_PREFIX + "w";
    // private static final String AUTH = COMMAND_PREFIX + "auth";

    private final Socket socket;
    private final ChatServer chatServer;
    private final DataInputStream in;
    private final DataOutputStream out;

    private String nick;

    public ClientHandler(Socket socket, ChatServer chatServer) {
        try {
            this.nick = "";
            this.socket = socket;
            this.chatServer = chatServer;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            new Thread(() -> {
                try {
                    authenticate();
                    readMessage();
                } finally {
                    closeConnection();
                }
            }).start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeConnection() {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (socket != null) {
            try {
                socket.close();
                chatServer.unSubscribe(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void readMessage() {
        try {
            while (true) {
                final String message = in.readUTF();
                if (message.startsWith("/")) {
                    if (message.equals("/end")) {
                        break;
                    }
                    if (message.startsWith("/w")) {
                        final String[] split = message.split(" ");
                        final String nickTo = split[1];
                        chatServer.sendMessageToClient(this, nickTo, message.substring("/w".length() + 2
                                + nickTo.length()));
                    }
                    continue;
                }
                chatServer.broadcast(nick + " " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    //Не забыть добавить функционал проверки входящих данных
    private void authenticate() {
        while (true) {
            try {
                final String message = in.readUTF();
                if (message.startsWith("/auth")) {
                    final String[] split = message.split(" ");
                    final String login = split[1];
                    final String password = split[2];

                    final String nick = chatServer.getAuthService().getNickByLoginAndPassword(login, password);
                    if (nick != null) {
                        if (chatServer.isNickBusy(nick)) {
                            sendMessage("Пользователь уже авторизован");
                            continue;
                        }
                        sendMessage("/authok " + nick);
                        this.nick = nick;
                        chatServer.broadcast("Пользователь " + nick + " зашел в чат");
                        chatServer.subscribe(this);
                        break;
                    } else {
                        sendMessage("Неверный логин или пароль");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String msg) {
        try {
            if(msg.startsWith("/authok")) {
                out.writeUTF(msg);
            } else {
                out.writeUTF(getNick() + ": " + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNick() {
        return nick == null ? "" : nick;
    }
}
