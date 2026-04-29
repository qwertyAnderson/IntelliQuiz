package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // UPDATE THESE IF YOUR MYSQL DETAILS ARE DIFFERENT
    private static final String URL = "jdbc:mysql://localhost:3306/intelliquiz";
    private static final String USER = "root";
    private static final String PASSWORD = "Sql@01";

    private static Connection con = null;

    public static Connection getConnection() {

        if (con != null) {
            return con;
        }

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Create connection
            con = DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("Database Connected Successfully!");

        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found!");
            System.out.println("Error: " + e.getMessage());

        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            System.out.println("Error: " + e.getMessage());
        }

        return con;
    }
}