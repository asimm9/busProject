package org.example.helper;
import java.sql.*;

public class DatabaseConnector {

    private static final String DB_URL = "jdbc:sqlite:D:\\AsiM\\yazilim\\busProject\\rezervasyon.db";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
