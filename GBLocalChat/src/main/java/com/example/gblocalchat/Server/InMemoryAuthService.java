package com.example.gblocalchat.Server;

import com.example.gblocalchat.JdbcBaseHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InMemoryAuthService implements AuthService {

    JdbcBaseHandler base = new JdbcBaseHandler();

    public InMemoryAuthService() {
        try {
            base.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getNickByLoginAndPassword(String login, String password) throws SQLException {
        return base.select(login,password);
    }
}
