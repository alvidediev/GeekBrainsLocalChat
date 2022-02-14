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
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS clients (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "login TEXT," +
                "password TEXT," +
                "nick TEXT" + ");");
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
            ResultSet resultSet = ps.executeQuery();
            return resultSet.getString(1);
        }
    }
}
