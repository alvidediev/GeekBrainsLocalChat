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

//    private static class UserData{
//        private final String login;
//        private final String password;
//        private final String nick;
//
//        public String getLogin() {
//            return login;
//        }
//
//        public String getPassword() {
//            return password;
//        }
//
//        public String getNick() {
//            return nick;
//        }
//
//        public UserData(String login, String password, String nick) {
//            this.login = login;
//            this.password = password;
//            this.nick = nick;
//
//
//        }
//    }
}
