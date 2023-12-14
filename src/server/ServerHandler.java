/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author w
 */
public class ServerHandler extends Thread {
    public Vector<ServerHandler> playersSocket = new Vector<>(); //maybe Set
    public DataInputStream in;
    public PrintStream out;
    public Socket socket;
    boolean isRunning= true;
    
    public ServerHandler(Socket socket) {

        try {
            this.socket= socket;
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
                String gson = in.readLine();
                if (!gson.isEmpty())
                    handleRequest(gson);
            }
        } catch (IOException ex) {
            try {
                out.close();
                in.close();
                socket.close();
                isRunning= false;
            } catch (IOException ex1) {
                Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    private void handleRequest(String json) {
        
        switch (1) {
            case 1:
                //TODO register();
                break;
            case 2:
                //TODO login();
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
}
