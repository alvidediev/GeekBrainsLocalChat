package com.example.gblocalchat.Server;

import com.example.gblocalchat.JdbcBaseHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InMemoryAuthService implements AuthService {

    JdbcBaseHandler base = new JdbcBaseHandler();

    public InMemoryAuthService() {
        try {
            ChatServer.LOGGER.info("Попытка подключения к базе данных");
            base.connect();
            ChatServer.LOGGER.info("Подключение к базе данных прошло успешно");
        } catch (SQLException e) {
            ChatServer.LOGGER.error("Серверу не удалось подключиться к базе данных ", e);
        }
    }

    @Override
    public String getNickByLoginAndPassword(String login, String password) {
        try {
            ChatServer.LOGGER.info("Попытка взять nick по логину - {} и паролю - {}", login, password);
            return base.select(login,password);
        } catch (SQLException e) {
            ChatServer.LOGGER.error("Не удалось взять ник из базы по таким логину и паролю  ", e);
        }
        return null;
    }
}
