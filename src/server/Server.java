/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Constants;

/**
 *
 * @author w
 */
public class Server implements Runnable {

    ServerSocket myServerSocket;
    Thread thread;
    boolean isRunning;
    {
        isRunning= true;
    }
    
    public Server() {
        startConnection();
    }

    private void startConnection() {
        try {
            myServerSocket = new ServerSocket(Constants.PORT_NUMBER);

            thread= new Thread(this);
            thread.start();
        } catch (IOException e) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                Socket socket = myServerSocket.accept();
                new ServerHandler(socket);
            }
        } catch (IOException e) {
            try {
                isRunning= false;
                myServerSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        new Server();
    }
}
