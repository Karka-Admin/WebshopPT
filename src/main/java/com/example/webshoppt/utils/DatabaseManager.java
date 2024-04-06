package com.example.webshoppt.utils;

import lombok.Getter;
import lombok.Setter;

import java.sql.*;

@Getter
@Setter
public class DatabaseManager {
    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public DatabaseManager() {
        this.connection = null;
        this.statement = null;
        this.preparedStatement = null;
        this.resultSet = null;
    }

    public void openConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/webshopptdb","root", "");
            System.out.println("SQL: Connection Opened.");
        } catch (Exception errors) {
            errors.printStackTrace();
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("SQL: Connection Closed.");
            } catch (Exception errors) {
                errors.printStackTrace();
            }
        }
    }

    public void sendStatementQuery(String query) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (Exception errors) {
            errors.printStackTrace();
        }
    }

    public void sendPreparedStatementQuery(PreparedStatement preparedStatement) {
        try {
            preparedStatement.execute();
        } catch (Exception errors) {
            errors.printStackTrace();
        }
    }
}
