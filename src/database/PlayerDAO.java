/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

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
public class PlayerDAO {

    public static int authenticatePlayer(Player player) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        int authenticateId = -1;

        try {
            String query = "SELECT id FROM player WHERE email = ? AND password = ?";
            preparedStatement = Database.connection.prepareStatement(query);
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
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String query = "SELECT isOnline FROM player WHERE id = ?";
            preparedStatement = Database.connection.prepareStatement(query);
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
        PreparedStatement preparedStatement = null;

        try {
            String availableQuery = "update player set isOnline = ? , isAvailable= ? where id= ?";
            preparedStatement = Database.connection.prepareStatement(availableQuery);
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
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;
        try {
            String query = "INSERT INTO player (name, email, password, isOnline, isAvailable, score) VALUES (?, ?, ?, ?, ?, ?)";
            preparedStatement = Database.connection.prepareStatement(query);
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

    public static ArrayList<Player> getAvailablePlayers() {
        PreparedStatement preparedStatement = null;
        ArrayList<Player> players = new ArrayList<>();
        ResultSet rs = null;

        try {
            preparedStatement = Database.connection.prepareStatement(
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
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String query = "SELECT name FROM player WHERE id = ?";
            preparedStatement = Database.connection.prepareStatement(query);
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
        boolean isFriend = false;
        try (PreparedStatement statement
                = Database.connection.prepareStatement("INSERT INTO friends (player_id, friend_id) VALUES (?, ?)")) {

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

    public static boolean removeFriend(int playerId, int friendId) {
        boolean isNotFriend = false;
        try (PreparedStatement statement = Database.connection.prepareStatement("DELETE FROM friends WHERE (player_id = ? AND friend_id = ?)")) {

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
        boolean isBlocked = false;
        try (PreparedStatement statement
                = Database.connection.prepareStatement("INSERT INTO blocks (player_id, blocked_id) VALUES (?, ?), (?, ?)")) {

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
        boolean isUnBlocked = false;
        try (PreparedStatement statement
                = Database.connection.prepareStatement("DELETE FROM blocks WHERE (player_id = ? AND blocked_id = ?) OR (player_id = ? AND blocked_id = ?)")) {

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
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String query = "SELECT name,SCORE FROM player WHERE id = ?";
            preparedStatement = Database.connection.prepareStatement(query);
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

    public static Player getDataOfPlayer(int playerId) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Player player = null;

        try {
            String query = "SELECT name, email, password FROM PLAYER WHERE id = ?";
            preparedStatement = Database.connection.prepareStatement(query);
            preparedStatement.setInt(1, playerId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                player = new Player(resultSet.getString("name"), resultSet.getString("email"), resultSet.getString("password"));

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
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            preparedStatement = Database.connection.prepareStatement("UPDATE ROOT.PLAYER SET NAME = ?, email = ?, password = ? WHERE ID =" + player.getId());

            preparedStatement.setString(1, player.getName());

            preparedStatement.setString(2, player.getEmail());

            preparedStatement.setString(3, player.getPassword());

            // preparedStatement.setInt(4, player.getId());
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static int getPlayerScore(int playerId) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int result = 0;

        try {
            String query = "SELECT score FROM player WHERE id = ?";
            preparedStatement = Database.connection.prepareStatement(query);
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
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int rowAffected = 0;
        String query;
        try {
            if (type == 1) {
                query = "UPDATE player SET score = SCORE+3 WHERE id = ?";

            } else {
                query = "UPDATE player SET score = score+1 WHERE id = ?";
            }
            preparedStatement = Database.connection.prepareStatement(query);
            preparedStatement.setInt(1, srcplayerId);
            rowAffected = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update player Score");
        } finally {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);
        }
        return rowAffected;
    }

    public static boolean setNotAvailable(int playerId) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int rowsAffected = 0;
        try {
            String query = "UPDATE player SET isAvailable = ? WHERE id = ?";
            preparedStatement = Database.connection.prepareStatement(query);
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
