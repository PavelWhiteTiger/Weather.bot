package com.usue.weather.bot;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String PASS = "0000";
    private static final String USER = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/TelegramWeather";
    @Getter
    private static final Connection CONNECTION;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            CONNECTION = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
