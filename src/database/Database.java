package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    public static final String DB_NAME= "TicTacToeDB";
    private static final String JDBC_URL = "jdbc:derby://localhost:1527/TicTacToeDB";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private static Connection connection;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            closeConnection();
        }));
    }
    
    private Database() {
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("org.apache.derby.jdbc.ClientDriver");
                connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to connect to the database");
            }
        }
        return connection;
    }

    // محدش يناديها علشان بتزعل وهتزعلنا
    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
                System.out.println("Connection closed");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connection = null;
            }
        }
    }
}
