/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strategy;

import database.PlayerDAO;
import server.ServerHandler;
import util.Constants;
import util.JsonHandler;


/**
 *
 * @author w
 */
public class SendMessageStrategy implements RequestHandlerStrategy {
    @Override
    public void handleRequest(ServerHandler serverHandler) {
        sendMessage(serverHandler);
    }
    
    private void sendMessage(ServerHandler serverHandler) {
        String message = serverHandler.requestData.get(1);
        int destinationId = Integer.valueOf(serverHandler.requestData.get(2));

        ServerHandler destinationSocket = serverHandler.getDestinationSocket(destinationId);
        String srcPlayerName = PlayerDAO.getPlayerName(serverHandler.playerId);

        String gsonRequest = JsonHandler.serializeJson(String.valueOf(Constants.SEND_MESSAGE),
                message, srcPlayerName);

        destinationSocket.out.println(gsonRequest);
    }
}
