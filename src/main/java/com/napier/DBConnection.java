package com.napier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection getConnection() throws SQLException {

        String host = System.getenv()
                .getOrDefault("DB_HOST", "localhost");

        String port = System.getenv()
                .getOrDefault("DB_PORT", "33060");

        String database = System.getenv()
                .getOrDefault("DB_NAME", "world");

        String username = System.getenv()
                .getOrDefault("DB_USER", "devops");

        String password = System.getenv()
                .getOrDefault("DB_PASSWORD", "devops");

        String url = "jdbc:mysql://" + host + ":"
                + port + "/" + database;

        System.out.println("Connecting to: " + url);

        return DriverManager.getConnection(url, username, password);
    }
}