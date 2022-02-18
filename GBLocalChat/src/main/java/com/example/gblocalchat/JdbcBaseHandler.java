package com.example.gblocalchat;

import java.sql.*;

public class JdbcBaseHandler {
    private Connection connection;
    private Statement statement;

    public static void main(String[] args) {
        JdbcBaseHandler base = new JdbcBaseHandler();
        try {
            base.connect();
            base.createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            base.close();
        }
    }

    private void close() {
        try {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:javadb.db");
        statement = connection.createStatement();
    }

    public void createTable() throws SQLException {
        statement.executeUpdate(
                "" + "CREATE TABLE IF NOT EXISTS clients (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "login TEXT UNIQUE," +
                        "password TEXT," +
                        "nick TEXT" + ");"
        );
    }


    public void insert(String login, String password, String nick) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO clients (login,password,nick) VALUES (?,?,?)"
        )) {
            ps.setString(1, login);
            ps.setString(2, password);
            ps.setString(3, nick);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String select(String login, String password) throws SQLException {
        try (final PreparedStatement ps = connection.prepareStatement(
                "SELECT nick FROM clients WHERE login = ? AND password = ?"
        )) {
            ps.setString(1, login);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
            return null;
        }
    }

    /**
     * В нашем окне не хватает уже места для для поле смены ника.
     * Описание метода и моей логики.
     * Поля:
     * @param login    - запись в базе данных (ник) который будет обновлен (изменен).
     * @param nick     - ключ, по которому будет производиться поиск записи.
     * Логика: По плану должно было быть окошко изменения ника в котором были бы собственно сами поля ввода (TextField)
     * кнопка смены ника и кнопка возврата на главное меню.
     */
    public void changeNick(String nick, String login) {
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE clients SET nick = ? WHERE login = ?"
        )) {
            ps.setString(1, nick);
            ps.setString(2, login);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
