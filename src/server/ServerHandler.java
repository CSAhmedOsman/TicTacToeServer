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
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Player;
import util.Constants;
import java.lang.reflect.Type;
import java.util.Vector;

import model.Message;

import java.util.HashSet;
import java.util.Set;

import java.util.HashSet;
import java.util.Set;
import util.Database;

/**
 *
 * @author w
 */
public class ServerHandler extends Thread {

    private static final Vector<ServerHandler> PLAYERS_SOCKET = new Vector(); //maybe Set
    public Socket socket;
    public DataInputStream in;
    public PrintStream out;
    boolean isRunning = true;
    int playerId;
    Gson gson = new Gson();
    ArrayList requestData;

    public ServerHandler(Socket socket) {

        try {
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new PrintStream(socket.getOutputStream());
            PLAYERS_SOCKET.add(this);

        } catch (IOException e) {
            e.printStackTrace();
        }

        start();
    }

    @Override
    public void run() {
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

        Type listType = new TypeToken<ArrayList<Object>>() {
        }.getType();
        requestData = gson.fromJson(gsonRequest, listType);

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
            case Constants.REQUEST:
                request();
                break;
            case Constants.SENDMESSAGE:
                sendMessage();
                break;
            case Constants.BROADCAST_MESSAGE:
                sendMessageToAll();
                break;
            case Constants.SETDATAOFPLAYER:
                getData();
                break;
            case Constants.UPDATEUSERPROFILE:
                updateUserProfile();
                break;
            case Constants.SENDINVITE:
                sendInvit();
                break;
            case Constants.ACCEPTGAME:
                acceptGame();
                break;
            case Constants.SENDMOVE:
                gameHandler();
                break;
            case Constants.UPDATESCORE:
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
        }
    }

    private void register() throws JsonSyntaxException {
        Player newPlayer = gson.fromJson(gson.toJson(requestData.get(1)), Player.class);
        boolean isRegisterd = Database.registerPlayer(newPlayer);

        ArrayList jsonResponse = new ArrayList();
        jsonResponse.add(Constants.REGISTER);
        jsonResponse.add(isRegisterd);

        String gsonRequest = gson.toJson(jsonResponse);
        out.println(gsonRequest);
    }

    private void login() throws JsonSyntaxException {
        Player currentplayer = gson.fromJson(gson.toJson(requestData.get(1)), Player.class);

        int authenticatePlayerId = Database.authenticatePlayer(currentplayer);

        if (authenticatePlayerId != -1) {
            playerId = authenticatePlayerId;
        }

                // Constants Better Than Java Doc
        if (Database.isOnline(authenticatePlayerId)) {
            authenticatePlayerId = -2;
        }
        ArrayList<Integer> jsonResponse = new ArrayList();
        jsonResponse.add(Constants.LOGIN);
        jsonResponse.add(authenticatePlayerId);
        String gsonResponse = gson.toJson(jsonResponse);
        out.println(gsonResponse);
    }

    private void getData() {
        double playerID = (double) requestData.get(1);
        Player player = Database.getDataOfPlayer((int)playerID);
        System.out.println("player Data :" + player.getEmail() + " " + player.getName() + " " + player.getPassword());
        ArrayList<Object> jsonResponse = new ArrayList();
        jsonResponse.add(Constants.SETDATAOFPLAYER);

        jsonResponse.add(player.getName());
        jsonResponse.add(player.getEmail());
        jsonResponse.add(player.getPassword());
        System.out.println("player Data :" + player.getEmail() + " " + player.getName() + " " + player.getPassword());

        String gsonRequest = gson.toJson(jsonResponse);
        out.println(gsonRequest);
    }

    public void updateUserProfile() {
      Player currentplayer = gson.fromJson(gson.toJson(requestData.get(1)), Player.class);

        if (Database.updateUserProfile(currentplayer)) {
            System.out.println("update User Profile");

        }

    }

    private void getAvailablePlayers() {
        System.out.println("getAvailablePlayers from server");
        ArrayList<Player> players = Database.getAvailablePlayers();
        ArrayList<Object> jsonResponse = new ArrayList();
        jsonResponse.add(Constants.GET_AVAILIABLE_PLAYERS);

        for (Player player : players) {
            jsonResponse.add((double) player.getId());
            jsonResponse.add(player.getName());
            jsonResponse.add((double) player.getScore());
            System.out.println("player Data :" + player.getId() + " " + player.getName() + " " + player.getScore());
        }

        String gsonRequest = gson.toJson(jsonResponse);
        out.println(gsonRequest);
    }

    private void sendMessageToAll() {
        double sourceId = (double) requestData.get(1);
        String broadCastMessage = (String) requestData.get(2);

        String sourcePlayerName = Database.getPlayerName((int) sourceId);

        ArrayList<Object> jsonResponse = new ArrayList();
        jsonResponse.add(Constants.BROADCAST_MESSAGE);
        jsonResponse.add(sourcePlayerName);
        jsonResponse.add(broadCastMessage);
        String gsonResponse = gson.toJson(jsonResponse);
        PLAYERS_SOCKET.forEach((serverHandler) -> {
            serverHandler.out.println(gsonResponse);
        });
    }

    private void request() {
        double senderId = (double) requestData.get(1);
        double receiverId = (double) requestData.get(2);
        Player p = Database.getPlayerNameAndScore((int) senderId);
        System.out.println("player Data :" + (int) senderId + " " + p.getName() + " " + p.getScore());
        //Player player = new Player((int)senderId, p.getEmail(), p.getPassword());
        ArrayList<Object> jsonResponse = new ArrayList<>();
        jsonResponse.add(Constants.REQUEST);
        jsonResponse.add(senderId);
        jsonResponse.add(p.getName());
        jsonResponse.add(p.getScore());
        String gsonResponse = gson.toJson(jsonResponse);
        out.println(gsonResponse);
        System.out.println("Request received from: " + senderId + " to: " + receiverId);

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

    private void sendMessage() {
        Message message = gson.fromJson(gson.toJson(requestData.get(1)), Message.class);

        ServerHandler destinationSocket = getDestinationSocket(message.getDestinationId());
        String srcPlayerName = Database.getPlayerName(message.getSourceId());

        ArrayList<Object> jsonArr = new ArrayList();
        jsonArr.add(Constants.SENDMESSAGE);
        jsonArr.add(message.getMessage());
        jsonArr.add(srcPlayerName);

        String gsonRequest = gson.toJson(jsonArr);

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
//------------------------abdelrhman-----------------------

    private void sendInvit() {

        GameInfo info = gson.fromJson(gson.toJson(requestData.get(1)), GameInfo.class);
        double type = (double) requestData.get(2);
        ServerHandler destinationSocket = getDestinationSocket(info.getDestPlayerId());

        if (destinationSocket != null && !destinationSocket.isInterrupted()) {

            ArrayList<Object> jsonArr = new ArrayList<>();

            jsonArr.add(Constants.SENDINVITE);
            jsonArr.add(gson.toJson(info));
            jsonArr.add(type);

            String gsonRequest = gson.toJson(jsonArr);
            destinationSocket.out.println(gsonRequest);
        } else {
            System.err.println("Destination socket is null. Cannot send invitation.");
        }

    }

    private void acceptGame() {

        double srcPlayerId = (double) requestData.get(1);
        double destPlayerId = (double) requestData.get(2);
        double type = (double) requestData.get(3);

        int srcId = (int) srcPlayerId;
        int destId = (int) destPlayerId;

        Database.setNotAvailable(srcId);
        Database.setNotAvailable(destId);

        ServerHandler sourceSocket = getDestinationSocket(srcId);
        ServerHandler destinationSocket = getDestinationSocket(destId);

        String player1Name = Database.getPlayerName(srcId);
        String player2Name = Database.getPlayerName(destId);

        int player1Score = Database.getPlayerScore(srcId);
        int player2Score = Database.getPlayerScore(destId);

        GameInfo info = new GameInfo(player2Name, player1Name, destId, srcId, player2Score, player1Score);

        ArrayList<Object> jsonPlayer1 = new ArrayList<>();
        jsonPlayer1.add(Constants.ACCEPTGAME);
        jsonPlayer1.add(gson.toJson(info));
        jsonPlayer1.add(true);
        jsonPlayer1.add(type);

        String gsonRequest1 = gson.toJson(jsonPlayer1);
        destinationSocket.out.println(gsonRequest1);

        info = new GameInfo(player1Name, player2Name, srcId, destId, player1Score, player2Score);

        ArrayList<Object> jsonPlayer2 = new ArrayList<>();
        jsonPlayer2.add(Constants.ACCEPTGAME);
        jsonPlayer2.add(gson.toJson(info));
        jsonPlayer2.add(false);
        jsonPlayer2.add(type);
        String gsonRequest2 = gson.toJson(jsonPlayer2);
        sourceSocket.out.println(gsonRequest2);
    }

    private void gameHandler() {
        String playable = (String) requestData.get(1);
        double x = (double) requestData.get(2);
        double y = (double) requestData.get(3);
        double destPlayerId = (double) requestData.get(4);

        ServerHandler destinationSocket = getDestinationSocket((int) destPlayerId);

        ArrayList<Object> jsonArr = new ArrayList<>();
        jsonArr.add(Constants.SENDMOVE);
        jsonArr.add(playable);
        jsonArr.add((int) x);
        jsonArr.add((int) y);
        String gsonRequest = gson.toJson(jsonArr);
        destinationSocket.out.println(gsonRequest);
    }

    private void hanleScore() {
        GameInfo info = gson.fromJson(gson.toJson(requestData.get(1)), GameInfo.class);
        double type = (double) requestData.get(2);
        Database.updatePlayerScore(info.getSrcPlayerId(), (int) type);
    }

    private void handleExit() {
        double srcPlayerId = (double) requestData.get(1);
        ServerHandler sourceSocket = getDestinationSocket((int) srcPlayerId);
        ArrayList<Object> jsonArr = new ArrayList<>();
        jsonArr.add(Constants.EXIT_PLAYER_GAME);
        String gsonRequest = gson.toJson(jsonArr);
        sourceSocket.out.println(gsonRequest);

    }

    private void addFriend() {
        double playerId = (double) requestData.get(1);
        double friendId = (double) requestData.get(2);

        boolean isFriend = Database.addFriend((int) playerId, (int) friendId);
        ArrayList jsonResponse = new ArrayList();
        jsonResponse.add(Constants.ADD_FRIEND);
        jsonResponse.add(isFriend);

        String gsonRequest = gson.toJson(jsonResponse);
        out.println(gsonRequest);
    }

    private void removeFriend() {
        double playerId = (double) requestData.get(1);
        double friendId = (double) requestData.get(2);

        boolean isNotFriend = Database.removeFriend((int) playerId, (int) friendId);
        ArrayList jsonResponse = new ArrayList();
        jsonResponse.add(Constants.REMOVE_FRIEND);
        jsonResponse.add(isNotFriend);

        String gsonRequest = gson.toJson(jsonResponse);
        out.println(gsonRequest);
    }

    private void blockPlayer() {
        double playerId = (double) requestData.get(1);
        double blockedId = (double) requestData.get(2);

        removeFriend();
        boolean isBlocked = Database.blockPlayer((int) playerId, (int) blockedId);
        ArrayList jsonResponse = new ArrayList();
        jsonResponse.add(Constants.BLOCK_PLAYER);
        jsonResponse.add(isBlocked);

        String gsonRequest = gson.toJson(jsonResponse);
        out.println(gsonRequest);
    }

    private void unBlockPlayer() {
        double playerId = (double) requestData.get(1);
        double blockedId = (double) requestData.get(2);

        boolean isUnBlocked = Database.unBlockPlayer((int) playerId, (int) blockedId);
        ArrayList jsonResponse = new ArrayList();
        jsonResponse.add(Constants.UN_BLOCK_PLAYER);
        jsonResponse.add(isUnBlocked);

        String gsonRequest = gson.toJson(jsonResponse);
        out.println(gsonRequest);
    }

    private void makePlayerOnline() {
        double playerId = (double) requestData.get(1);

        Database.makePlayerOnline((int) playerId);
    }
}
