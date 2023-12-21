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
        isRunning = true;
    }

    public Server() throws IOException {
        startConnection();
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
            for (ServerHandler s : ServerHandler.playersSocket) {
                s.in.close();
                s.out.close();
                s.socket.close();
            }
            isRunning = false;
            myServerSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
}
