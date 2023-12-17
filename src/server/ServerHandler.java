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

/**
 *
 * @author w
 */
public class ServerHandler extends Thread {

    public Vector<ServerHandler> playersSocket = new Vector(); //maybe Set
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        playersSocket.add(this);
        start();
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                String gsonRequest = in.readLine();
                if (!gsonRequest.isEmpty()) {
                    System.out.println("Handle Responce");
                    handleRequest(gsonRequest);
                }
            }
        } catch (IOException ex) {
            try {
                out.close();
                in.close();
                socket.close();
                isRunning = false;
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
            case 10:
                // TODO sendMessage();
                break;
        }
    }
    
    private void register() throws JsonSyntaxException {
        System.out.println("Regsiter Function In The Server");
        Player newPlayer = gson.fromJson(gson.toJson(requestData.get(1)), Player.class);
        //Add This New Player To DB
        boolean isRegisterd = true;
        //boolean isRegisterd = addToDB(newPlayer);

        ArrayList jsonArr = new ArrayList();
        jsonArr.add(Constants.REGISTER);
        jsonArr.add(isRegisterd);

        String gsonRequest = gson.toJson(jsonArr);
        System.out.println("Serialized JSON: " + gsonRequest);
        out.println(gsonRequest);
    }

    private void login() throws JsonSyntaxException {
        System.out.println("Login Function In The Server");
        Player currentplayer = gson.fromJson(gson.toJson(requestData.get(1)), Player.class);
        int playerId= 2;
        //checkInDB();

        ArrayList<Integer> jsonArr = new ArrayList();
        jsonArr.add(Constants.LOGIN);
        jsonArr.add(playerId);

        String gsonRequest = gson.toJson(jsonArr);
        System.out.println("Ana Hna And Al Login Server");
        System.out.println("Serialized JSON: " + gsonRequest);
        out.println(gsonRequest);
    }

    private boolean checkInDB() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean addToDB(Player newPlayer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
