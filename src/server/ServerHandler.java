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
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Player;
import util.Constants;
import java.lang.reflect.Type;
import model.Message;
import util.Database;

/**
 *
 * @author w
 */
public class ServerHandler extends Thread {

    public static Vector<ServerHandler> playersSocket = new Vector(); //maybe Set
    private int playerId;
    public DataInputStream in;
    public PrintStream out;
    public Socket socket;
    boolean isRunning = true;

    Gson gson = new Gson();
    ArrayList requestData;

    public ServerHandler(Socket socket) {

        try {
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new PrintStream(socket.getOutputStream());
            playersSocket.add(this);
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
                if (!gsonRequest.isEmpty())
                    handleRequest(gsonRequest);
            }
        } catch (IOException ex) {
            try {
                out.close();
                in.close();
                socket.close();
                isRunning = false;
                
                playersSocket.remove(this);
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
        System.out.println(currentplayer.getEmail()+" "+ currentplayer.getPassword());
        int authenticatePlayerId= Database.authenticatePlayer(currentplayer);

        ArrayList<Integer> jsonArr = new ArrayList();
        jsonArr.add(Constants.LOGIN);
        jsonArr.add(authenticatePlayerId);

        String gsonRequest = gson.toJson(jsonArr);
        out.println(gsonRequest);
    }

    private void sendMessage() {
        Message message = gson.fromJson(gson.toJson(requestData.get(1)), Message.class);
        ServerHandler destinationSocket = getDestinationPlayerSocket(message.getDestinationId());
        String destinationPlayerName = Database.getPlayerName(message.getSourceId());
        
        ArrayList<Object> jsonArr = new ArrayList();
        jsonArr.add(Constants.SENDMESSAGE);
        jsonArr.add(message.getMessage());
        jsonArr.add(destinationPlayerName);
        
        String gsonRequest = gson.toJson(jsonArr);
        destinationSocket.out.println(gsonRequest);
    }

    private ServerHandler getDestinationPlayerSocket(int destinationId) {
        ServerHandler destinationHandler = null;
        for (ServerHandler serverHandler : playersSocket) {
            if(destinationId== serverHandler.playerId)
                destinationHandler = serverHandler;
        }
        return destinationHandler;
    }
}