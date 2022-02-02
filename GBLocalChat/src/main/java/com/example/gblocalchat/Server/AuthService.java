package com.example.gblocalchat.Server;

public interface AuthService {
    String getNickByLoginAndPassword(String login, String password);
}
