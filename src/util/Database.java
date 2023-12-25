/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Player;

/**
 *
 * @author w
 */
public class Database {

    private static final String JDBC_URL = "jdbc:derby://localhost:1527/TicTacToeDB";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private static Connection connection;

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

    public static int authenticatePlayer(Player player) {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        int authenticateId = -1;

        try {
            String query = "SELECT id FROM player WHERE email = ? AND password = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, player.getEmail());
            preparedStatement.setString(2, player.getPassword());

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                authenticateId = resultSet.getInt("id");

                String availableQuery = "update player set isOnline = ? , isAvailable= ? where id= ?";
                preparedStatement = connection.prepareStatement(availableQuery);
                preparedStatement.setBoolean(1, true);
                preparedStatement.setBoolean(2, true);
                preparedStatement.setInt(3, authenticateId);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected <= 0) {
                    authenticateId = -1;
                    System.out.println("Is Available Problem");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Authentication failed");
        } finally {
            if (resultSet != null) {
                closeResultSet(resultSet);
            }
            if (preparedStatement != null) {
                closeStatement(preparedStatement);
            }
        }
        return authenticateId;
    }

    public static boolean registerPlayer(Player player) {
        connection = getConnection();
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;
        try {
            String query = "INSERT INTO player (name, email, password, isOnline, isAvailable, score) VALUES (?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, player.getName());
            preparedStatement.setString(2, player.getEmail());
            preparedStatement.setString(3, player.getPassword());
            preparedStatement.setBoolean(4, false);
            preparedStatement.setBoolean(5, false);
            preparedStatement.setInt(6, 0);
            try{
                rowsAffected = preparedStatement.executeUpdate();
            } catch (Exception exception) {
                rowsAffected = 0;
                System.err.println(exception.getMessage());
            }
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Player registration failed");
        } finally {
            closeStatement(preparedStatement);
        }
        return rowsAffected > 0;
    }

    public static ArrayList<Player> getAvailablePlayers() {
        System.out.println("getAvailablePlayers from database");
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        ArrayList<Player> players = new ArrayList<>();
        ResultSet rs = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT id, NAME, SCORE FROM PLAYER WHERE ISAVAILABLE = true",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            );

            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("NAME");
                int score = rs.getInt("SCORE");

                Player player = new Player(id, name, score);
                players.add(player);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return players;
    }
    
    public static String getPlayerName(int playerId) {
        connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String query = "SELECT name FROM player WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, playerId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("name");
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get player name");
        } finally {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);
        }
    }
    
    public static boolean addFriend(int playerId, int friendId) {
        connection = getConnection();
        
        boolean isFriend = false;
        try (PreparedStatement statement = 
                connection.prepareStatement("INSERT INTO friends (player_id, friend_id) VALUES (?, ?)")) {

            statement.setInt(1, playerId);
            statement.setInt(2, friendId);

            int rowAffected= statement.executeUpdate();
            if(rowAffected>0)
                isFriend= true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isFriend;
    }
    
    public static boolean removeFriend(int playerId, int friendId) {
        connection= getConnection();
        boolean isNotFriend= false;
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM friends WHERE (player_id = ? AND friend_id = ?)")) {

            statement.setInt(1, playerId);
            statement.setInt(2, friendId);

            int rowAffected = statement.executeUpdate();
            if(rowAffected> 0)
                isNotFriend = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isNotFriend;
    }
        
    public static boolean blockPlayer(int playerId, int blockedId) {
        connection = getConnection();
        
        boolean isBlocked = false;
        try (PreparedStatement statement = 
                connection.prepareStatement("INSERT INTO blocks (player_id, blocked_id) VALUES (?, ?), (?, ?)")) {

            statement.setInt(1, playerId);
            statement.setInt(2, blockedId);
            statement.setInt(3, blockedId);
            statement.setInt(4, playerId);

            int rowAffected= statement.executeUpdate();
            if(rowAffected>0)
                isBlocked = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isBlocked;
    }
    
    public static boolean unBlockPlayer(int playerId, int unblockedId) {
        connection= getConnection();
        boolean isUnBlocked = false;
        try (PreparedStatement statement = 
                connection.prepareStatement("DELETE FROM blocks WHERE (player_id = ? AND blocked_id = ?) OR (player_id = ? AND blocked_id = ?)")) {

            statement.setInt(1, playerId);
            statement.setInt(2, unblockedId);
            statement.setInt(3, unblockedId);
            statement.setInt(4, playerId);

            int rowAffected = statement.executeUpdate();
            if(rowAffected> 0)
                isUnBlocked = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isUnBlocked;
    }
    
    public static Player getPlayerNameAndScore(int playerId) {
        connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String query = "SELECT name,SCORE FROM player WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, playerId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Player(resultSet.getString("name"), resultSet.getInt("SCORE"));

            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get player name");
        } finally {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);
        }
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

    private static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void closeStatement(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
