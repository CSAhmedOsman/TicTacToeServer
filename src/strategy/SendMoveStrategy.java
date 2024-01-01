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
public class SendMoveStrategy implements RequestHandlerStrategy {
    @Override
    public void handleRequest(ServerHandler serverHandler) {
        sendMove(serverHandler);
    }
    
    private void sendMove(ServerHandler serverHandler) {
        String playable = serverHandler.requestData.get(1);
        int x = Integer.valueOf(serverHandler.requestData.get(2));
        int y = Integer.valueOf(serverHandler.requestData.get(3));
        int destPlayerId = Integer.valueOf(serverHandler.requestData.get(4));

        ServerHandler destinationSocket = serverHandler.getDestinationSocket(destPlayerId);

        String gsonRequest = JsonHandler.serializeJson(String.valueOf(Constants.SEND_MOVE), playable,
                String.valueOf(x), String.valueOf(y));
        destinationSocket.out.println(gsonRequest);
    }
}