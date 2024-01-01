/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strategy;

import database.PlayerDAO;
import server.PlayersSocketManager;
import server.ServerHandler;
import util.Constants;
import util.JsonHandler;


/**
 *
 * @author w
 */
public class SendBroadcastStrategy implements RequestHandlerStrategy {
    @Override
    public void handleRequest(ServerHandler serverHandler) {
        sendBroadcastMessage(serverHandler);
    }
    
    private void sendBroadcastMessage(ServerHandler serverHandler) {
        String broadCastMessage = serverHandler.requestData.get(1);

        String srcPlayerName = PlayerDAO.getPlayerName(serverHandler.playerId);

        String gsonResponse = JsonHandler.serializeJson(String.valueOf(Constants.BROADCAST_MESSAGE), srcPlayerName, broadCastMessage);

        PlayersSocketManager.getPlayersSocket().forEach((targetSocket) -> {
            targetSocket.out.println(gsonResponse);
        });
    }
}
