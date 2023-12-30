/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import model.GameInfo;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Player;
import util.Constants;
import database.PlayerDAO;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Vector;
import util.JsonHandler;

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
    ArrayList<String> requestData;
    
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
        }catch (IOException e) {
            e.printStackTrace();
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
                if (!gsonRequest.isEmpty()) {
                    handleRequest(gsonRequest);
                }
            }
        } catch (IOException ex) {
            try {
                out.close();
                in.close();
                socket.close();
                isRunning = false;
                PLAYERS_SOCKET.remove(this);

            } catch (IOException ex1) {
                Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    private void handleRequest(String gsonRequest) {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        requestData = JsonHandler.deserializeArray(gsonRequest, listType);

        int action =  Integer.valueOf(requestData.get(0));
        switch (action) {
            case Constants.REGISTER:
                register();
                break;
            case Constants.LOGIN:
                login();
                break;
            case Constants.GET_AVAILIABLE_PLAYERS:
                getAvailablePlayers();
                break;
            case Constants.REQUEST:
                request();
                break;
            case Constants.SENDMESSAGE:
                sendMessage();
                break;
            case Constants.BROADCAST_MESSAGE:
                sendBroadcastMessage();
                break;
            case Constants.SET_DATA_OF_PLAYER:
                getData();
                break;
            case Constants.UPDATEUSERPROFILE:
                updateUserProfile();
                break;
            case Constants.SENDINVITE:
                sendInvit();
                break;
            case Constants.ACCEPT_GAME:
                acceptGame();
                break;
            case Constants.SEND_MOVE:
                gameHandler();
                break;
            case Constants.UPDATESCORE:
                handleScore();
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
        }
    }

    private void register() throws JsonSyntaxException {
        Type playerType = new TypeToken<Player>() {}.getType();
        Player newPlayer = JsonHandler.deserializeArray(requestData.get(1), playerType);
        boolean isRegistered = PlayerDAO.registerPlayer(newPlayer);

        String gsonRequest = JsonHandler.serializeJson(String.valueOf(Constants.REGISTER), Boolean.toString(isRegistered));
        out.println(gsonRequest);
    }

    /**
     * Send To Client 
     * Positive => Found 
     * Not Online -1 
     * Not Found -2 => Found But Online
     *
     * @throws JsonSyntaxException
     */
    private void login() throws JsonSyntaxException {
        Type playerType = new TypeToken<Player>() {}.getType();
        Player currentPlayer = JsonHandler.deserializeArray(requestData.get(1), playerType);

        int authenticatePlayerId = PlayerDAO.authenticatePlayer(currentPlayer);

        if (authenticatePlayerId != Constants.PLAYER_NOT_EXIST) {
            playerId = authenticatePlayerId;
        }

        if (PlayerDAO.isOnline(authenticatePlayerId)) {
            authenticatePlayerId = Constants.PLAYER_ONLINE;
        }

        String gsonResponse = JsonHandler.serializeJson(String.valueOf(Constants.LOGIN), String.valueOf(authenticatePlayerId));
        out.println(gsonResponse);
    }

    private void getData() {
        Player player = PlayerDAO.getDataOfPlayer(playerId);
        
        String gsonRequest = JsonHandler.serializeJson(String.valueOf(Constants.SET_DATA_OF_PLAYER), 
                player.getName(), player.getEmail(), player.getPassword());
        out.println(gsonRequest);
    }

    public void updateUserProfile() {
        Type playerType = new TypeToken<Player>() {}.getType();
        Player currentPlayer = JsonHandler.deserializeArray(requestData.get(1), playerType);
        //Player currentplayer = gson.fromJson(gson.toJson(requestData.get(1)), Player.class);

        if (PlayerDAO.updateUserProfile(currentPlayer)) {
            System.out.println("update User Profile");
        }
    }

    private void getAvailablePlayers() {
        ArrayList<Player> players = PlayerDAO.getAvailablePlayers();

        String gsonResponse = JsonHandler.serializeJson(String.valueOf(Constants.GET_AVAILIABLE_PLAYERS),
                                                        JsonHandler.serelizeObject(players));
        out.println(gsonResponse);
    }

    private void sendBroadcastMessage() {
        String broadCastMessage = requestData.get(1);

        String srcPlayerName = PlayerDAO.getPlayerName(playerId);

        String gsonResponse = JsonHandler.serializeJson(String.valueOf(Constants.BROADCAST_MESSAGE), srcPlayerName, broadCastMessage);

        PLAYERS_SOCKET.forEach((serverHandler) -> {
            serverHandler.out.println(gsonResponse);
        });
    }

    private void request() {
        int destinationId = Integer.valueOf(requestData.get(1));
        ServerHandler serverHandler = getDestinationSocket(destinationId);
        Player player = PlayerDAO.getPlayerNameAndScore(playerId);

        String gsonResponse = JsonHandler.serializeJson(String.valueOf(Constants.REQUEST), 
                String.valueOf(playerId), player.getName(), String.valueOf(player.getScore()));

        serverHandler.out.println(gsonResponse);
    }

    private void sendMessage() {
        String message = requestData.get(1);
        int destinationId = Integer.valueOf(requestData.get(2));
        
        ServerHandler destinationSocket = getDestinationSocket(destinationId);
        String srcPlayerName = PlayerDAO.getPlayerName(playerId);

        String gsonRequest = JsonHandler.serializeJson(String.valueOf(Constants.SENDMESSAGE),
                message, srcPlayerName);

        destinationSocket.out.println(gsonRequest);
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

    private void sendInvit() {

        Type gameType = new TypeToken<GameInfo>() {}.getType();
        GameInfo info = JsonHandler.deserializeArray(requestData.get(1), gameType);
        
        int type = Integer.valueOf(requestData.get(2));
        ServerHandler destinationSocket = getDestinationSocket(info.getDestPlayerId());

        if (destinationSocket != null && !destinationSocket.isInterrupted()) {
            String gsonRequest = JsonHandler.serializeJson(String.valueOf(Constants.SENDINVITE),
                    JsonHandler.serelizeObject(info), String.valueOf(type));
            
            destinationSocket.out.println(gsonRequest);
        } else {
            System.err.println("Destination socket is null. Cannot send invitation.");
        }
    }

    private void acceptGame() {
        int destPlayerId  = Integer.valueOf(requestData.get(1));
        int type  = Integer.valueOf(requestData.get(2));

        PlayerDAO.setNotAvailable(playerId);
        PlayerDAO.setNotAvailable(destPlayerId);

        ServerHandler sourceSocket = getDestinationSocket(playerId);
        ServerHandler destinationSocket = getDestinationSocket(destPlayerId);

        String player1Name = PlayerDAO.getPlayerName(playerId);
        String player2Name = PlayerDAO.getPlayerName(destPlayerId);

        int player1Score = PlayerDAO.getPlayerScore(playerId);
        int player2Score = PlayerDAO.getPlayerScore(destPlayerId);

        GameInfo info = new GameInfo(player2Name, player1Name, destPlayerId, playerId, player2Score, player1Score);

        String gsonRequest = JsonHandler.serializeJson(String.valueOf(Constants.ACCEPT_GAME), 
                JsonHandler.serelizeObject(info), String.valueOf(true), String.valueOf(type));
        destinationSocket.out.println(gsonRequest);

        info = new GameInfo(player1Name, player2Name, playerId, destPlayerId, player1Score, player2Score);

        gsonRequest = JsonHandler.serializeJson(String.valueOf(Constants.ACCEPT_GAME),
                JsonHandler.serelizeObject(info), String.valueOf(false), String.valueOf(type));
        sourceSocket.out.println(gsonRequest);
    }

    private void gameHandler() {
        String playable = requestData.get(1);
        int x  = Integer.valueOf(requestData.get(2));
        int y  = Integer.valueOf(requestData.get(3));
        int destPlayerId = Integer.valueOf(requestData.get(4));

        ServerHandler destinationSocket = getDestinationSocket(destPlayerId);

        String gsonRequest = JsonHandler.serializeJson(String.valueOf(Constants.SEND_MOVE), playable, 
                String.valueOf(x), String.valueOf(y));
        destinationSocket.out.println(gsonRequest);
    }

    private void handleScore() {
        Type gameType = new TypeToken<GameInfo>() {}.getType();
        GameInfo info = JsonHandler.deserializeArray(requestData.get(1), gameType);
        int type = Integer.valueOf(requestData.get(2));
        PlayerDAO.updatePlayerScore(info.getSrcPlayerId(), type);
    }

    private void handleExit() {
        ServerHandler sourceSocket = getDestinationSocket(playerId);
        String gsonRequest = JsonHandler.serializeJson(String.valueOf(Constants.EXIT_PLAYER_GAME));
        sourceSocket.out.println(gsonRequest);
    }

    private void addFriend() {
        int friendId = Integer.valueOf(requestData.get(1));

        boolean isFriend = PlayerDAO.addFriend(playerId, friendId);

        String gsonRequest = JsonHandler.serializeJson(String.valueOf(Constants.ADD_FRIEND), String.valueOf(isFriend));
        out.println(gsonRequest);
    }

    private void removeFriend() {
        int friendId = Integer.valueOf(requestData.get(1));

        boolean isNotFriend = PlayerDAO.removeFriend(playerId, friendId);

        String gsonRequest = JsonHandler.serializeJson(String.valueOf(Constants.REMOVE_FRIEND), String.valueOf(isNotFriend));
        out.println(gsonRequest);
    }

    private void blockPlayer() {
        int blockedId = Integer.valueOf(requestData.get(1));

        PlayerDAO.removeFriend(playerId, blockedId);
        boolean isBlocked = PlayerDAO.blockPlayer(playerId, blockedId);

        String gsonRequest = JsonHandler.serializeJson(String.valueOf(Constants.BLOCK_PLAYER), String.valueOf(isBlocked));
        out.println(gsonRequest);
    }

    private void unBlockPlayer() {
        int blockedId = Integer.valueOf(requestData.get(1));

        boolean isUnBlocked = PlayerDAO.unBlockPlayer(playerId, blockedId);

        String gsonRequest = JsonHandler.serializeJson(String.valueOf(Constants.UN_BLOCK_PLAYER), String.valueOf(isUnBlocked));
        out.println(gsonRequest);
    }

    private void makePlayerOnline() {
        PlayerDAO.makePlayerOnline(playerId);
    }

    public static void closeSockets() {
        try {
            for (ServerHandler serverHandler : PLAYERS_SOCKET) {
                serverHandler.in.close();
                serverHandler.out.close();
                serverHandler.socket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}