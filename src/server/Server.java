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
    private static Server singletonServer;

    {
        isRunning = true;
    }

    private Server() {
    }

    public static Server getServer() throws IOException {
        if (singletonServer == null) {
            singletonServer = new Server();
            singletonServer.startConnection();
        }
        return singletonServer;
    }

    private void startConnection() throws IOException {
        myServerSocket = new ServerSocket(Constants.PORT_NUMBER);
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                Socket socket = myServerSocket.accept();
                System.out.println("is Running Client 1");
                new ServerHandler(socket);
            }
        } catch (IOException e) {
            try {
                isRunning = false;
                myServerSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void close() {
        try {
            ServerHandler.closeSockets();
            myServerSocket.close();
            isRunning = false;
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void connect() throws IOException {
        if (isRunning == false) {
            startConnection();
            isRunning = true;
        }
    }

    public boolean isRunning() {
        return isRunning;
    }
}
