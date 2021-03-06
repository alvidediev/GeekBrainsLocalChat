package com.example.gblocalchat.Client;

import com.example.gblocalchat.Command;
import com.example.gblocalchat.HelloController;
import com.example.gblocalchat.JdbcBaseHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.gblocalchat.Command.*;

public class ChatClient {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private HelloController controller;
    JdbcBaseHandler base = new JdbcBaseHandler();

    private final String LOCAL_HOST = "localhost";
    private final int PORT = 8189;

    public ChatClient(HelloController controller) {
        this.controller = controller;
        openConnection();
    }

    private void openConnection() {
        try {
            socket = new Socket(LOCAL_HOST, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            new Thread(() -> {
                try {
                    while (true) {
                        final String authMsg = in.readUTF();
                        if (getCommandByText(authMsg) == AUTHOK) {
                            final String nick = authMsg.split(" ")[1];
                            controller.addMessage("Успешная авторизация под ником " + nick);
                            controller.setAuth(true);
                            controller.loadHistory();
                            break;
                        }
                    }

                    while (true) {
                        final String message = in.readUTF();
                        if (isCommand(message)) {
                            if (getCommandByText(message) == END) {
                                controller.setAuth(false);
                                break;
                            }
                            if (getCommandByText(message) == CLIENTS) {
                                final String[] clients = message.replace(CLIENTS.getCommand() + " ", "")
                                        .split(" ");
                                controller.updateClientList(clients);
                            }
                            if(getCommandByText(message) == CHANGE_NICK){
                                base.connect();
                                final String[] split = message.replace(CHANGE_NICK.getCommand() + " ", "").split(" ");
                                final String nick = split[1];
                                final String login = split[2];
                                base.changeNick(nick,login);
                            }
                        }
                        controller.addMessage(message);
                        controller.saveHistory();
                    }
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
