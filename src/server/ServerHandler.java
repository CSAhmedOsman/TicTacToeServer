/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

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
import model.Message;
import database.PlayerDAO;
import java.lang.reflect.Type;
import java.util.Vector;
import util.JsonHandler;

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
        Type listType = new TypeToken<ArrayList<Object>>() {}.getType();
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
            case Constants.REQUEST:
                request();
                break;
            case 5:
                //TODO accept();
                break;
            case 6:
                //TODO updateBoard();
                break;
            case 7:
                //TODO logout();
                break;
            case 8:
                // TODO save();
                break;
            case 9:
                //TODO finish();
                break;

            case Constants.SENDMESSAGE:
                sendMessage();
                break;
            case 11:
                //getAvailablePlayers();
                break;
            case Constants.BROADCAST_MESSAGE:
                sendBroadcastMessage();
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
        }
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
        Type playerType = new TypeToken<ArrayList<Player>>() {
        }.getType();
        Player currentPlayer = JsonHandler.deserializeArray((String) requestData.get(1), playerType);

        int authenticatePlayerId = PlayerDAO.authenticatePlayer(currentPlayer);

        if (authenticatePlayerId != Constants.PLAYER_NOT_EXIST) {
            playerId = authenticatePlayerId;
        }

        if (PlayerDAO.isOnline(authenticatePlayerId)) {
            authenticatePlayerId = Constants.PLAYER_ONLINE;
        }

        String gsonResponse = JsonHandler.serializeJson(Constants.LOGIN, authenticatePlayerId);
        out.println(gsonResponse);
    }

    private void getAvailablePlayers() {
        ArrayList<Player> players = PlayerDAO.getAvailablePlayers();

        String gsonResponse = JsonHandler.serializeJson(Constants.GET_AVAILIABLE_PLAYERS, 
                                                        gson.toJson(players));
        out.println(gsonResponse);
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

    private void request() {
        double srcId = (double) requestData.get(1);
        double destinationId = (double) requestData.get(2);
        ServerHandler serverHandler = getDestinationSocket((int) destinationId);
        Player p = PlayerDAO.getPlayerNameAndScore((int) srcId);

        String gsonResponse = JsonHandler.serializeJson(Constants.REQUEST, srcId, p.getName(), p.getScore());

        serverHandler.out.println(gsonResponse);
    }

    private void sendMessage() {
        Message message = gson.fromJson(gson.toJson(requestData.get(1)), Message.class);

        ServerHandler destinationSocket = getDestinationSocket(message.getDestinationId());
        String srcPlayerName = PlayerDAO.getPlayerName(message.getSourceId());

        String gsonRequest = JsonHandler.serializeJson(Constants.SENDMESSAGE,
                message.getMessage(), srcPlayerName);

        destinationSocket.out.println(gsonRequest);
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