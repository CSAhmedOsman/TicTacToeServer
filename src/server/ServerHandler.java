/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.google.gson.reflect.TypeToken;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Constants;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import util.JsonHandler;
import strategy.*;

/**
 *
 * @author w
 */
public class ServerHandler extends Thread {

    private final Map<Integer, RequestHandlerStrategy> strategies = new HashMap<>();
    private boolean isRunning = true;
    private Socket socket;
    private DataInputStream in;
    public PrintStream out;
    public int playerId;
    public ArrayList<String> requestData;

    public ServerHandler(Socket socket) {
        makeNewSocket(socket);
        populateStrategies();
        start();
    }

    private void populateStrategies() {
            strategies.put(Constants.REGISTER, new RegisterStrategy());
            strategies.put(Constants.LOGIN, new LoginStrategy());
            strategies.put(Constants.GET_AVAILIABLE_PLAYERS, new GetAvailableStrategy());
            strategies.put(Constants.SEND_MESSAGE, new SendMessageStrategy());
            strategies.put(Constants.BROADCAST_MESSAGE, new SendBroadcastStrategy());
            strategies.put(Constants.GET_DATA_OF_PLAYER, new GetDataStrategy());
            strategies.put(Constants.UPDATE_USER_PROFILE, new UpdateProfileStrategy());
            strategies.put(Constants.SEND_INVITE, new SendInviteStrategy());
            strategies.put(Constants.ACCEPT_GAME, new AcceptGameStrategy());
            strategies.put(Constants.SEND_MOVE, new SendMoveStrategy());
            strategies.put(Constants.UPDATE_SCORE, new UpdateScoreStrategy());
            strategies.put(Constants.EXIT_GAME, new ExitGameStrategy());
            strategies.put(Constants.ADD_FRIEND, new AddFriendStrategy());
            strategies.put(Constants.REMOVE_FRIEND, new RemoveFriendStrategy());
            strategies.put(Constants.BLOCK_PLAYER, new BlockPlayerStrategy());
            strategies.put(Constants.UN_BLOCK_PLAYER, new UnblockPlayerStrategy());
            strategies.put(Constants.ONLINE, new MakeOnlineStrategy());
    }
    
    public void setRequestStrategy(int key, RequestHandlerStrategy requestStrategy) {
        strategies.put(key, requestStrategy);
    }
    
    private void makeNewSocket(Socket socket) {
        try {
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new PrintStream(socket.getOutputStream());
            PlayersSocketManager.getPlayersSocket().add(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        startListeneing();
    }

    private void startListeneing() {
        try {
            while (isRunning) {
                String gsonRequest = in.readLine();
                if (!gsonRequest.isEmpty()) {
                    handleRequest(gsonRequest);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
            closeSocket();
        }
    }

    private void handleRequest(String gsonRequest) {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        requestData = JsonHandler.deserializeArray(gsonRequest, listType);

        int action = Integer.valueOf(requestData.get(0));
        RequestHandlerStrategy request = strategies.get(action);
        if (request != null) {
            request.handleRequest(this);
        }
    }

    public ServerHandler getDestinationSocket(int destinationId) {
        ServerHandler destinationHandler = null;
        for (ServerHandler currentSocket : PlayersSocketManager.getPlayersSocket()) {
            if (destinationId == currentSocket.playerId) {
                destinationHandler = currentSocket;
                break;
            }
        }
        return destinationHandler;
    }

    static void closeSockets() {
        PlayersSocketManager.getPlayersSocket().forEach((serverHandler) -> {
            serverHandler.closeSocket();
        });
    }

    private void closeSocket() {
        try {
            out.close();
            in.close();
            socket.close();
            isRunning = false;
            PlayersSocketManager.getPlayersSocket().remove(this);
        } catch (IOException ex1) {
            Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }
}