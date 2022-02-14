package com.example.gblocalchat.Server;

import java.sql.SQLException;

public interface AuthService {
    String getNickByLoginAndPassword(String login, String password) throws SQLException;
}
