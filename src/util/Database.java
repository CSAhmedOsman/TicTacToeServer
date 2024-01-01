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
import java.util.List;
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

    public static Connection connection;

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

    public static boolean isOnline(int playerId) {
        connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String query = "SELECT isOnline FROM player WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, playerId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBoolean("isOnline");
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get isOnline");
        } finally {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);
        }
    }

    public static void makePlayerOnline(int authenticateId) {
        connection = getConnection();
        PreparedStatement preparedStatement = null;
        try {
            String availableQuery = "update player set isOnline = ? , isAvailable= ? where id= ?";
            preparedStatement = connection.prepareStatement(availableQuery);
            preparedStatement.setBoolean(1, true);
            preparedStatement.setBoolean(2, true);
            preparedStatement.setInt(3, authenticateId);
            int rowsAffected = preparedStatement.executeUpdate();
            System.err.println("Here: " + rowsAffected);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
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
            try {
                rowsAffected = preparedStatement.executeUpdate();
            } catch (Exception exception) {
                rowsAffected = 0;
                System.err.println(exception.getMessage());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Player registration Failed");
        } finally {
            closeStatement(preparedStatement);
        }
        return rowsAffected > 0;
    }

    public static Player getDataOfPlayer(int playerId) {
        connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Player player = null;

        try {
            String query = "SELECT  name, email, SCORE FROM PLAYER WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, playerId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                player = new Player(resultSet.getString("name"), resultSet.getString("email"), resultSet.getInt("SCORE"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get player name");
        } finally {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);
        }
        return player;
    }

    public static boolean updateUserProfile(Player player) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement("UPDATE ROOT.PLAYER SET NAME = ?, email = ?, password = ? WHERE ID =" + player.getId());

            preparedStatement.setString(1, player.getName());

            preparedStatement.setString(2, player.getEmail());

            preparedStatement.setString(3, player.getPassword());

            System.out.println("DataBase");
            System.out.print(player.getName() + " " + player.getEmail() + " " + player.getPassword() + " " + player.getId());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            try {
                if (rs != null) {
                    rs.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static ArrayList<Player> getAvailablePlayers(int playerId) {
        System.out.println("getAvailablePlayers from database");
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        ArrayList<Player> players = new ArrayList<>();
        ResultSet rs = null;

        try {

            preparedStatement = connection.prepareStatement(
                    "SELECT p.NAME, p.SCORE, p.id FROM player p WHERE p.id <> ? AND NOT EXISTS (\n"
                    + "    SELECT 1 FROM blocks b WHERE (b.player_id = ? AND b.blocked_id = p.id) OR (b.player_id = p.id AND b.blocked_id = ?) )\n"
                    + "ORDER BY CASE WHEN p.ISAVAILABLE AND EXISTS (\n"
                    + "      SELECT 1 FROM friends f WHERE (f.player_id = ? AND f.friend_id = p.id) ) THEN 1 ELSE 0 END DESC, p.ISAVAILABLE DESC, ( SELECT COUNT(*) FROM friends f WHERE (f.player_id = ? AND f.friend_id = p.id) OR (f.player_id = p.id AND f.friend_id = ?) ) DESC, p.score DESC",
                    //"SELECT NAME, SCORE, id FROM player WHERE ISAVAILABLE = true AND ISONLINE = true",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            );

            preparedStatement.setInt(1, playerId);
            preparedStatement.setInt(2, playerId);
            preparedStatement.setInt(3, playerId);
            preparedStatement.setInt(4, playerId);
            preparedStatement.setInt(5, playerId);
            preparedStatement.setInt(6, playerId);

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

    public static ArrayList<Integer> getBlockPlayers(int playerId) {
        System.out.println("getBlockPlayers from database");
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        ArrayList<Integer> Blocked = new ArrayList<>();
        ResultSet rs = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT blocked_id FROM blocks WHERE is_Blocked= true AND player_id=?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            );

            preparedStatement.setInt(1, playerId);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("blocked_id");
                Blocked.add(id);
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

        return Blocked;
    }

    public static String getPlayerName(int playerId) {
        connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String name = null;
        try {
            String query = "SELECT name FROM player WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, playerId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                name = resultSet.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get player name");
        } finally {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);
        }
        return name;
    }

    public static boolean addFriend(int playerId, int friendId) {
        connection = getConnection();

        if (areFriends(playerId, friendId)) {

            return false;
        }

        boolean isFriend = false;
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO FRIENDS (player_id, friend_id) VALUES (?, ?)")) {
            statement.setInt(1, playerId);
            statement.setInt(2, friendId);

            int rowAffected = statement.executeUpdate();
            if (rowAffected > 0) {
                isFriend = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isFriend;
    }

    private static boolean areFriends(int playerId, int friendId) {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT 1 FROM FRIENDS WHERE (player_id = ? AND friend_id = ?) OR (player_id = ? AND friend_id = ?)")) {
            statement.setInt(1, playerId);
            statement.setInt(2, friendId);
            statement.setInt(3, friendId);
            statement.setInt(4, playerId);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Player> getFriends(int playerId) {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        List<Player> friends = new ArrayList<>();

        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT p.NAME, p.SCORE, p.id FROM player p "
                    + "INNER JOIN friends f ON (p.id = f.friend_id AND f.player_id = ?) OR (p.id = f.player_id AND f.friend_id = ?)"
            );
            preparedStatement.setInt(1, playerId);
            preparedStatement.setInt(2, playerId);

            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("NAME");
                int score = rs.getInt("SCORE");

                Player friend = new Player(id, name, score);
                friends.add(friend);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeResources(rs, preparedStatement);
        }

        return friends;
    }

    // Your existing methods...
    private static void closeResources(ResultSet rs, PreparedStatement preparedStatement) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean removeFriend(int playerId, int friendId) {
        connection = getConnection();
        boolean isNotFriend = false;
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM friends WHERE (player_id = ? AND friend_id = ?)")) {

            statement.setInt(1, playerId);
            statement.setInt(2, friendId);

            int rowAffected = statement.executeUpdate();
            if (rowAffected > 0) {
                isNotFriend = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isNotFriend;
    }

    public static boolean blockPlayer(int playerId, int blockedId) {
        connection = getConnection();

        boolean isBlocked = false;
        try (PreparedStatement statement
                = connection.prepareStatement("INSERT INTO blocks (player_id, blocked_id, is_blocked) VALUES (?, ?, true), (?, ?, true)")) {

            statement.setInt(1, playerId);
            statement.setInt(2, blockedId);
            statement.setInt(3, blockedId);
            statement.setInt(4, playerId);

            int rowAffected = statement.executeUpdate();
            if (rowAffected > 0) {
                isBlocked = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isBlocked;
    }

    public static boolean unBlockPlayer(int playerId, int unblockedId) {
        connection = getConnection();
        boolean isUnBlocked = false;
        try (PreparedStatement statement
                = connection.prepareStatement("DELETE FROM blocks WHERE (player_id = ? AND blocked_id = ?) OR (player_id = ? AND blocked_id = ?)")) {

            statement.setInt(1, playerId);
            statement.setInt(2, unblockedId);
            statement.setInt(3, unblockedId);
            statement.setInt(4, playerId);

            int rowAffected = statement.executeUpdate();
            if (rowAffected > 0) {
                isUnBlocked = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isUnBlocked;
    }

    public static Player getPlayerNameAndScore(int playerId) {
        System.out.println("get data of Player from database");
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

    public static int getPlayerScore(int playerId) {
        connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int result = 0;

        try {
            String query = "SELECT score FROM player WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, playerId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                result = resultSet.getInt("score");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get player Score");
        } finally {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);
        }
        return result;
    }

    public static int updatePlayerScore(int srcplayerId, int type) {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int result = 0;
        try {
            if (type == 1) {
                String query = "UPDATE player SET score = SCORE+3 WHERE id = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, srcplayerId);
                int rowsAffected = preparedStatement.executeUpdate();
            } else {
                String updateQuery = "UPDATE player SET score = score+1 WHERE id = ?";
                preparedStatement = connection.prepareStatement(updateQuery);
                preparedStatement.setInt(1, srcplayerId);
                int rowsAffected = preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update player Score");
        } finally {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);
        }
        return result;
    }

    public static boolean setNotAvailable(int playerId) {
        connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int rowsAffected = 0;
        try {
            String query = "UPDATE player SET isAvailable = ? WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setBoolean(1, false);
            preparedStatement.setInt(2, playerId);
            rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get player id");
        } finally {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);
        }
    }

    public static boolean logoutPlayer(int playerId) {
        connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int rowsUpdated = 0;
        try {
            String query = "UPDATE player SET isAvailable = false,ISONLINE = false WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, playerId);
            rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Update successful.");
            } else {
                System.out.println("No rows were updated.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get player id");
        } finally {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);
        }
        return rowsUpdated > 0;
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
