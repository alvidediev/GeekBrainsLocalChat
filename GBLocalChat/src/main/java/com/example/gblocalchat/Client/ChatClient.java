package com.example.gblocalchat.Client;

import com.example.gblocalchat.HelloController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatClient {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private HelloController controller;

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
                        if(authMsg.startsWith("/authok")){
                            final String nick = authMsg.split(" ")[1];
                            controller.addMessage("Успешная авторизация под ником " + nick);
                            break;
                        }
                    }

                    while (true){
                        final String message = in.readUTF();
                        if("/end".equals(message)){

                        }
                        controller.addMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
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
