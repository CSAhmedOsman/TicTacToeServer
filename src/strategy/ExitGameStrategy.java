/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strategy;

import server.ServerHandler;
import util.Constants;
import util.JsonHandler;


/**
 *
 * @author w
 */
public class ExitGameStrategy implements RequestHandlerStrategy {
    @Override
    public void handleRequest(ServerHandler serverHandler) {
        exitGame(serverHandler);
    }
    
    private void exitGame(ServerHandler serverHandler) {
        ServerHandler sourceSocket = serverHandler.getDestinationSocket(serverHandler.playerId);
        String gsonRequest = JsonHandler.serializeJson(String.valueOf(Constants.EXIT_GAME));
        sourceSocket.out.println(gsonRequest);
    }
}
