

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    public static Connection getConnection() throws ClassNotFoundException {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection to the database
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3305/pro", "root", "root@04");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createTablesIfNotExists() throws ClassNotFoundException {
        try (Connection conn = getConnection(); 
             Statement stmt = conn.createStatement()) {
            // Create the users table if it doesn't exist
            String createUsersTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "name VARCHAR(100), "
                    + "email VARCHAR(100) UNIQUE, "
                    + "password VARCHAR(100));";
            stmt.executeUpdate(createUsersTableSQL);

            // Create the cars table
            String createCarsTableSQL = "CREATE TABLE IF NOT EXISTS cars ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "model VARCHAR(100), "
                    + "year INT, "
                    + "rental_price DOUBLE);";
            stmt.executeUpdate(createCarsTableSQL);

            // Create the rentals table
            String createRentalsTableSQL = "CREATE TABLE IF NOT EXISTS rentals ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "user_id INT, "
                    + "car_id INT, "
                    + "rental_date DATE, "
                    + "return_date DATE, "
                    + "FOREIGN KEY (user_id) REFERENCES users(id), "
                    + "FOREIGN KEY (car_id) REFERENCES cars(id));";
            stmt.executeUpdate(createRentalsTableSQL);
            

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

