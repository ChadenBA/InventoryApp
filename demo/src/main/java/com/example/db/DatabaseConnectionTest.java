package com.example.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionTest {
    public static void main(String[] args) {
        String url = DatabaseConfig.getDatabaseUrl();
        String username = DatabaseConfig.getDatabaseUsername();
        String password = DatabaseConfig.getDatabasePassword();
  
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connected to the database!");
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database. Error: " + e.getMessage());
            e.printStackTrace();
        }
        
    }
     public static Connection getConnection() throws SQLException {
              String url = DatabaseConfig.getDatabaseUrl();
        String username = DatabaseConfig.getDatabaseUsername();
        String password = DatabaseConfig.getDatabasePassword();
        return DriverManager.getConnection(url, username, password);
    }

   
}
