package com.example.gblocalchat.Server;

import com.example.gblocalchat.Command;
import com.example.gblocalchat.HelloController;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

import static com.example.gblocalchat.Command.*;

public class ClientHandler {
    private final Socket socket;
    private final ChatServer chatServer;
    private final DataInputStream in;
    private final DataOutputStream out;

    private String nick;
    private boolean connect;

    public ClientHandler(Socket socket, ChatServer chatServer) {
        try {
            this.nick = "";
            connect = false;
            this.socket = socket;
            this.chatServer = chatServer;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    Thread.sleep(120_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!connect) {
                    closeConnection();
                }
            }).start();

            new Thread(() -> {
                try {
                    authenticate();
                    if (connect) {
                        readMessage();
                    }
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
                if (Command.isCommand(message)) {
                    if (getCommandByText(message) == END) {
                        break;
                    }
                    if (getCommandByText(message) == PRIVATE_MESSAGE) {
                        final String[] split = message.split(" ");
                        final String nickTo = split[1];
                        chatServer.sendMessageToClient(this, nickTo, message.substring("/w".length() + 2
                                + nickTo.length()));
                    }
                    continue;
                }
                chatServer.broadcast(nick + ": " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void authenticate() {
        while (true) {
            try {
                final String message = in.readUTF();
                if (getCommandByText(message) == AUTH) {
                    final String[] split = message.split(" ");
                    final String login = split[1];
                    final String password = split[2];
                    final String nick = chatServer.getAuthService().getNickByLoginAndPassword(login,password);
                    if (nick != null) {
                        if (chatServer.isNickBusy(nick)) {
                            sendMessage("Пользователь уже авторизован");
                            continue;
                        }
                        sendMessage(AUTHOK, nick);
                        this.nick = nick;
                        chatServer.broadcast("Пользователь " + nick + " зашел в чат");
                        chatServer.subscribe(this);
                        connect = true;
                        break;
                    } else {
                        sendMessage("Неверный логин или пароль");
                    }
                }
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(Command command, String msg) {
        try {
            out.writeUTF(command.getCommand() + " " + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg) {
        try {
            out.writeUTF(getNick() + ": " + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getNick() {
        return nick == null ? "" : nick;
    }
}
