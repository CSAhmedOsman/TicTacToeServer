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

    private final RequestStrategyManager strategyManager;
    private boolean isRunning= true;
    private Socket socket;
    private DataInputStream in;
    public PrintStream out;
    public int playerId;
    public ArrayList<String> requestData;

    {
        strategyManager = new RequestStrategyManager();
    }
    
    public ServerHandler(Socket socket) {
        makeNewSocket(socket);
        strategyManager.populateStrategies();
        start();
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
        Type listType = new TypeToken<ArrayList<String>>() {
        }.getType();
        requestData = JsonHandler.deserializeArray(gsonRequest, listType);

        int action = Integer.valueOf(requestData.get(0));
        RequestHandlerStrategy request = strategyManager.getRequestStrategy(action);
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