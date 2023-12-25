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
import java.lang.reflect.Type;
import java.util.Vector;
import model.Message;
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
            case 3:
                //TODO request();
                break;
            case 4:
                //TODO accept();
                break;
            case 5:
                //TODO updateBoard();
                break;
            case 6:
                //TODO logout();
                break;
            case 7:
                // TODO save();
                break;
            case 8:
                //TODO finish();
                break;
            case 9:
                //TODO updateScore();
                break;
            case Constants.SENDMESSAGE:
                sendMessage();
                break;
            case 11:
                //getAvailablePlayers();
                break;
            case Constants.BROADCAST_MESSAGE:
                sendMessageToAll();
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
            case Constants.PLAYER_ONLINE:
                makePlayerOnline();
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

    /**
     * Return
     * Positive => Found And Not Online
     * Negative -1 => Not Found
     * Else => Found But Online
     * 
     * @throws JsonSyntaxException 
     */
    private void login() throws JsonSyntaxException {
        Player currentplayer = gson.fromJson(gson.toJson(requestData.get(1)), Player.class);

        int authenticatePlayerId = Database.authenticatePlayer(currentplayer);
        
        if(authenticatePlayerId!= -1)
            playerId = authenticatePlayerId;
        
        // Constants Better Than Java Doc
        if(Database.isOnline(authenticatePlayerId))
            authenticatePlayerId = -2;
        
        ArrayList<Integer> jsonResponse = new ArrayList();
        jsonResponse.add(Constants.LOGIN);
        jsonResponse.add(authenticatePlayerId);

        String gsonResponse = gson.toJson(jsonResponse);
        out.println(gsonResponse);
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
        
        Database.makePlayerOnline((int)playerId);
    }
}
