/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import model.GameInfo;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import model.Player;
import util.Constants;
import database.PlayerDAO;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Vector;

import model.Message;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import util.JsonHandler;
import util.Util;

/**
 *
 * @author w
 */
public class ServerHandler extends Thread {

    private static final Vector<ServerHandler> PLAYERS_SOCKET; //maybe Set
    public Socket socket;
    public DataInputStream in;
    public PrintStream out;
    boolean isRunning = true;
    int playerId;
    Gson gson = new Gson();
    ArrayList requestData;

    static {
        PLAYERS_SOCKET = new Vector();
    }

    public ServerHandler(Socket socket) {

        makeNewSocket(socket);

        start();
    }

    private void makeNewSocket(Socket socket1) {
        try {
            this.socket = socket1;
            in = new DataInputStream(socket1.getInputStream());
            out = new PrintStream(socket1.getOutputStream());
            PLAYERS_SOCKET.add(this);
        } catch (IOException e) {
            Util.showDialog(Alert.AlertType.ERROR, "close Connection", "Error While open the Socket Connection");
        }
    }

    @Override
    public void run() {
        startListeneing();
    }

    public void startListeneing() {
        try {
            while (isRunning) {
                String gsonRequest = in.readLine();
                if (gsonRequest != null && !gsonRequest.isEmpty()) {
                    handleRequest(gsonRequest);
                } else {
                    close();
                }
            }
        } catch (IOException ex) {
            close();
        }
    }

    private void handleRequest(String gsonRequest) {
        Type listType = new TypeToken<ArrayList<Object>>() {
        }.getType();
        requestData = JsonHandler.deserializeArray(gsonRequest, listType);

        double action = (double) requestData.get(0);
        switch ((int) action) {
            case Constants.REGISTER:
                register();
                break;
            case Constants.LOGIN:
                login();
                break;
            case Constants.GET_AVAILIABLE_PLAYERS:
                getAvailablePlayers();
                break;
            case Constants.SEND_MESSAGE:
                sendMessage();
                break;
            case Constants.BROADCAST_MESSAGE:
                sendBroadcastMessage();
                break;
            case Constants.SETDATAOFPLAYER:
                getData();
                break;
            case Constants.UPDATEUSERPROFILE:
                updateUserProfile();
                break;
            case Constants.SEND_INVITE:
                sendInvit();
                break;
            case Constants.ACCEPT_INVITE:
                acceptInvite();
                break;
            case Constants.SEND_MOVE:
                gameHandler();
                break;
            case Constants.UPDATE_SCORE:
                hanleScore();
                break;
            case Constants.EXIT_PLAYER_GAME:
                handleExit();
                break;
            case Constants.ADD_FRIEND:
                addFriend();
                break;
            case Constants.REMOVE_FRIEND:
                removeFriend();
                break;
            case Constants.BLOCK_PLAYER:
                blockPlayer();
                break;
            case Constants.UN_BLOCK_PLAYER:
                unBlockPlayer();
                break;
            case Constants.ONLINE:
                makePlayerOnline();
                break;
            case Constants.BLOCKLIST:
                getBlockPlayers();
                break;
            case Constants.LOGOUT:
                logout();
                break;
            case Constants.REJECT_INVITE:
                rejectInvite();
                break;
        }
    }

    private void logout() {
        double playerID = (double) requestData.get(1);

        boolean isLogout = PlayerDAO.logoutPlayer((int) playerID);

        ArrayList jsonResponse = new ArrayList();
        jsonResponse.add(Constants.LOGOUT);
        jsonResponse.add(isLogout);

        String gsonRequest = gson.toJson(jsonResponse);
        out.println(gsonRequest);
        close();
    }

    private void register() throws JsonSyntaxException {
        Type playerType = new TypeToken<Player>() {
        }.getType();
        Player newPlayer = gson.fromJson((String) requestData.get(1), playerType);
        boolean isRegisterd = PlayerDAO.registerPlayer(newPlayer);

        String gsonRequest = JsonHandler.serializeJson(Constants.REGISTER, isRegisterd);
        out.println(gsonRequest);
    }

    /**
     * Send To Client Positive => Found And Not Online -1 => Not Found -2 =>
     * Found But Online
     *
     * @throws JsonSyntaxException
     */
    private void login() throws JsonSyntaxException {
        Type playerType = new TypeToken<Player>() {
        }.getType();
        String playerData = (String) requestData.get(1);
        Gson gson = new Gson();
        Player currentPlayer = gson.fromJson(playerData, playerType); //JsonHandler.deserializeArray(playerData, playerType);
        int authenticatePlayerId = PlayerDAO.authenticatePlayer(currentPlayer);
        if (authenticatePlayerId != Constants.PLAYER_NOT_EXIST) {
            playerId = authenticatePlayerId;
        }
        /*
        if (PlayerDAO.isOnline(authenticatePlayerId)) {
            authenticatePlayerId = Constants.PLAYER_ONLINE;
        }*/

        String gsonResponse = JsonHandler.serializeJson(Constants.LOGIN, authenticatePlayerId);
        out.println(gsonResponse);
    }

    private void getData() {
        double playerID = (double) requestData.get(1);

        Player player = PlayerDAO.getDataOfPlayer((int) playerID);
        ArrayList<Object> jsonResponse = new ArrayList();
        jsonResponse.add(Constants.SETDATAOFPLAYER);

        if (player != null) {
            jsonResponse.add(player.getName());
            jsonResponse.add(player.getEmail());
            jsonResponse.add(player.getScore());
        } else {
            jsonResponse.add("Player");
            jsonResponse.add("");
            jsonResponse.add(0);
        }

        String gsonRequest = gson.toJson(jsonResponse);
        out.println(gsonRequest);
    }

    public void updateUserProfile() {
        Player currentplayer = gson.fromJson(gson.toJson(requestData.get(1)), Player.class);
    }

    private void getAvailablePlayers() {
        double playerID = (double) requestData.get(1);
        ArrayList<Player> players = PlayerDAO.getAvailablePlayers((int) playerID);
        ArrayList<Object> jsonResponse = new ArrayList();
        jsonResponse.add(Constants.GET_AVAILIABLE_PLAYERS);

        for (Player player : players) {
            if (isFriend(player.getId())) {
                jsonResponse.add("true");
            } else {
                jsonResponse.add("false");
            }
            jsonResponse.add((double) player.getId());
            jsonResponse.add(player.getName());
            jsonResponse.add((double) player.getScore());
        }

        String gsonRequest = gson.toJson(jsonResponse);
        out.println(gsonRequest);
    }

    private boolean isFriend(int friendId) {
        List<Player> friends = PlayerDAO.getFriends(playerId);
        for (Player friend : friends) {
            if (friend.getId() == friendId) {
                return true;
            }
        }
        return false;
    }

    private void getBlockPlayers() {
        System.out.println("getBlockPlayers from server");
        ArrayList<Integer> blocked = PlayerDAO.getBlockPlayers(playerId);
        ArrayList<Object> jsonResponse = new ArrayList();
        jsonResponse.add(Constants.BLOCKLIST);

        if (!blocked.isEmpty()) {
            for (int id : blocked) {
                Player p = PlayerDAO.getPlayerNameAndScore(id);
                System.out.println("player Data :" + id + " " + p.getName() + " " + p.getScore());
                jsonResponse.add((double) id);
                jsonResponse.add(p.getName());
                jsonResponse.add((double) p.getScore());
            }
        }
        if (!jsonResponse.isEmpty()) {
            String gsonRequest = gson.toJson(jsonResponse);
            out.println(gsonRequest);
        }
    }

    private void sendBroadcastMessage() {
        double sourceId = (double) requestData.get(1);
        String broadCastMessage = (String) requestData.get(2);

        String srcPlayerName = PlayerDAO.getPlayerName((int) sourceId);

        String gsonResponse = JsonHandler.serializeJson(Constants.BROADCAST_MESSAGE, srcPlayerName, broadCastMessage);

        PLAYERS_SOCKET.forEach((serverHandler) -> {
            serverHandler.out.println(gsonResponse);
        });
    }

    private void sendMessage() {
        Message message = gson.fromJson(gson.toJson(requestData.get(1)), Message.class);

        ServerHandler destinationSocket = getDestinationSocket(message.getDestinationId());
        String srcPlayerName = PlayerDAO.getPlayerName(message.getSourceId());
        if (destinationSocket != null && !destinationSocket.isInterrupted()) {
            String gsonRequest = JsonHandler.serializeJson(Constants.SEND_MESSAGE, message.getMessage(), srcPlayerName);
            destinationSocket.out.println(gsonRequest);
        }
    }

    private ServerHandler getDestinationSocket(int destinationId) {
        ServerHandler destinationHandler = null;
        for (ServerHandler currentSocket : PLAYERS_SOCKET) {
            if (destinationId == currentSocket.playerId) {
                destinationHandler = currentSocket;
                break;
            }
        }
        return destinationHandler;
    }
//------------------------abdelrhman-----------------------

    private void sendInvit() {

        GameInfo info;
        double srcPlayerId = (double) requestData.get(1);
        double destPlayerId = (double) requestData.get(2);
        double type = (double) requestData.get(3);
        ServerHandler destinationSocket = getDestinationSocket((int) destPlayerId);

        if (destinationSocket != null && !destinationSocket.isInterrupted()) {
            String srcPlayerName = PlayerDAO.getPlayerName((int) srcPlayerId);
            String destPlayerName = PlayerDAO.getPlayerName((int) destPlayerId);
            int srcPlayerScore = PlayerDAO.getPlayerScore((int) srcPlayerId);
            int destPlayerScore = PlayerDAO.getPlayerScore((int) destPlayerId);

            info = new GameInfo(srcPlayerName, destPlayerName, (int) srcPlayerId, (int) destPlayerId, srcPlayerScore, destPlayerScore);

            ArrayList<Object> jsonArr = new ArrayList<>();
            jsonArr.add(Constants.SEND_INVITE);

            jsonArr.add(gson.toJson(info));
            jsonArr.add(type);

            String gsonRequest = gson.toJson(jsonArr);
            destinationSocket.out.println(gsonRequest);
        } else {
            System.err.println("Destination socket is null. Cannot send invitation.");
        }
    }

    private void acceptInvite() {

        double srcPlayerId = (double) requestData.get(1);
        double destPlayerId = (double) requestData.get(2);
        double type = (double) requestData.get(3);

        int srcId = (int) srcPlayerId;
        int destId = (int) destPlayerId;

        PlayerDAO.setNotAvailable(srcId);
        PlayerDAO.setNotAvailable(destId);

        ServerHandler sourceSocket = getDestinationSocket(srcId);
        ServerHandler destinationSocket = getDestinationSocket(destId);

        String player1Name = PlayerDAO.getPlayerName(srcId);
        String player2Name = PlayerDAO.getPlayerName(destId);

        int player1Score = PlayerDAO.getPlayerScore(srcId);
        int player2Score = PlayerDAO.getPlayerScore(destId);

        GameInfo info;
        if (destinationSocket != null && !destinationSocket.isInterrupted()) {

            info = new GameInfo(player2Name, player1Name, destId, srcId, player2Score, player1Score);

            ArrayList<Object> jsonPlayer1 = new ArrayList<>();
            jsonPlayer1.add(Constants.ACCEPT_INVITE);
            jsonPlayer1.add(gson.toJson(info));
            jsonPlayer1.add(true);
            jsonPlayer1.add(type);

            String gsonRequest1 = gson.toJson(jsonPlayer1);
            destinationSocket.out.println(gsonRequest1);
        }
        if (sourceSocket != null && !sourceSocket.isInterrupted()) {

            info = new GameInfo(player1Name, player2Name, srcId, destId, player1Score, player2Score);

            ArrayList<Object> jsonPlayer2 = new ArrayList<>();
            jsonPlayer2.add(Constants.ACCEPT_INVITE);
            jsonPlayer2.add(gson.toJson(info));
            jsonPlayer2.add(false);
            jsonPlayer2.add(type);
            String gsonRequest2 = gson.toJson(jsonPlayer2);
            sourceSocket.out.println(gsonRequest2);
        }
    }

    private void gameHandler() {
        String playable = (String) requestData.get(1);
        double x = (double) requestData.get(2);
        double y = (double) requestData.get(3);
        double destPlayerId = (double) requestData.get(4);

        ServerHandler destinationSocket = getDestinationSocket((int) destPlayerId);
        if (destinationSocket != null && !destinationSocket.isInterrupted()) {

            ArrayList<Object> jsonArr = new ArrayList<>();
            jsonArr.add(Constants.SEND_MOVE);
            jsonArr.add(playable);
            jsonArr.add((int) x);
            jsonArr.add((int) y);
            String gsonRequest = gson.toJson(jsonArr);
            destinationSocket.out.println(gsonRequest);
        }
    }

    private void hanleScore() {
        GameInfo info = gson.fromJson(gson.toJson(requestData.get(1)), GameInfo.class);
        double type = (double) requestData.get(2);
        PlayerDAO.updatePlayerScore(info.getSrcPlayerId(), (int) type);
    }

    private void handleExit() {
        double srcPlayerId = (double) requestData.get(1);
        ServerHandler sourceSocket = getDestinationSocket((int) srcPlayerId);

        if (sourceSocket != null && !sourceSocket.isInterrupted()) {
            ArrayList<Object> jsonArr = new ArrayList<>();
            jsonArr.add(Constants.EXIT_PLAYER_GAME);
            String gsonRequest = gson.toJson(jsonArr);
            sourceSocket.out.println(gsonRequest);
        }
    }

    private void addFriend() {
        double playerId = (double) requestData.get(1);
        double friendId = (double) requestData.get(2);

        boolean isFriend = PlayerDAO.addFriend((int) playerId, (int) friendId);

        String gsonRequest = JsonHandler.serializeJson(Constants.ADD_FRIEND, isFriend);
        out.println(gsonRequest);
    }

    private void removeFriend() {
        double playerId = (double) requestData.get(1);
        double friendId = (double) requestData.get(2);

        boolean isNotFriend = PlayerDAO.removeFriend((int) playerId, (int) friendId);

        String gsonRequest = JsonHandler.serializeJson(Constants.REMOVE_FRIEND, isNotFriend);
        out.println(gsonRequest);
    }

    private void blockPlayer() {
        double playerId = (double) requestData.get(1);
        double blockedId = (double) requestData.get(2);

        PlayerDAO.removeFriend((int) playerId, (int) blockedId);
        boolean isBlocked = PlayerDAO.blockPlayer((int) playerId, (int) blockedId);

        String gsonRequest = JsonHandler.serializeJson(Constants.BLOCK_PLAYER, isBlocked);
        out.println(gsonRequest);
    }

    private void unBlockPlayer() {
        double playerId = (double) requestData.get(1);
        double blockedId = (double) requestData.get(2);

        boolean isUnBlocked = PlayerDAO.unBlockPlayer((int) playerId, (int) blockedId);

        String gsonRequest = JsonHandler.serializeJson(Constants.UN_BLOCK_PLAYER, isUnBlocked);
        out.println(gsonRequest);
    }

    private void makePlayerOnline() {
        double playerId = (double) requestData.get(1);
        PlayerDAO.makePlayerOnline((int) playerId);
    }

    /*
    public static void closeSockets() {
        try {
            System.err.println(PLAYERS_SOCKET.size());
            for (ServerHandler serverHandler : PLAYERS_SOCKET) {
                System.err.println(PLAYERS_SOCKET.size());
                PlayerDAO.logoutPlayer(serverHandler.playerId);
                serverHandler.in.close();
                serverHandler.out.close();
                serverHandler.socket.close();
                PLAYERS_SOCKET.remove(serverHandler);
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }*/
    public static void closeSockets() {
        try {
            Iterator<ServerHandler> iterator = PLAYERS_SOCKET.iterator();
            while (iterator.hasNext()) {
                ServerHandler serverHandler = iterator.next();
                PlayerDAO.logoutPlayer(serverHandler.playerId);
                serverHandler.in.close();
                serverHandler.out.close();
                serverHandler.socket.close();
                iterator.remove(); // Use iterator to safely remove the current element
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void rejectInvite() {
        double srcPlayerId = (double) requestData.get(1);
        double destPlayerId = (double) requestData.get(2);
        double type = (double) requestData.get(3);

        int srcId = (int) srcPlayerId;
        int destId = (int) destPlayerId;

        PlayerDAO.setNotAvailable(srcId);
        PlayerDAO.setNotAvailable(destId);

        ServerHandler sourceSocket = getDestinationSocket(srcId);
        ServerHandler destinationSocket = getDestinationSocket(destId);

        String player1Name = PlayerDAO.getPlayerName(srcId);
        String player2Name = PlayerDAO.getPlayerName(destId);

        int player1Score = PlayerDAO.getPlayerScore(srcId);
        int player2Score = PlayerDAO.getPlayerScore(destId);

        GameInfo info;

        if (destinationSocket != null && !destinationSocket.isInterrupted()) {
            info = new GameInfo(player2Name, player1Name, destId, srcId, player2Score, player1Score);
            ArrayList<Object> jsonPlayer1 = new ArrayList<>();
            jsonPlayer1.add(Constants.REJECT_INVITE);
            jsonPlayer1.add(gson.toJson(info));
            jsonPlayer1.add(true);
            jsonPlayer1.add(type);

            String gsonRequest1 = gson.toJson(jsonPlayer1);
            destinationSocket.out.println(gsonRequest1);
        }

        if (sourceSocket != null && !sourceSocket.isInterrupted()) {
            info = new GameInfo(player1Name, player2Name, srcId, destId, player1Score, player2Score);
            ArrayList<Object> jsonPlayer2 = new ArrayList<>();
            jsonPlayer2.add(Constants.REJECT_INVITE);
            jsonPlayer2.add(gson.toJson(info));
            jsonPlayer2.add(false);
            jsonPlayer2.add(type);

            String gsonRequest2 = gson.toJson(jsonPlayer2);
            sourceSocket.out.println(gsonRequest2);
        }
    }

    private void close() {
        try {
            isRunning = false;
            PlayerDAO.logoutPlayer(playerId);
            out.close();
            in.close();
            socket.close();
            PLAYERS_SOCKET.remove(this);
            this.stop();
        } catch (IOException ex) {
            Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
