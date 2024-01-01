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
public class UnblockPlayerStrategy implements RequestHandlerStrategy {
    @Override
    public void handleRequest(ServerHandler serverHandler) {
        unBlockPlayer(serverHandler);
    }
    
    private void unBlockPlayer(ServerHandler serverHandler) {
        int blockedId = Integer.valueOf(serverHandler.requestData.get(1));

        boolean isUnBlocked = PlayerDAO.unBlockPlayer(serverHandler.playerId, blockedId);

        String gsonRequest = JsonHandler.serializeJson(String.valueOf(Constants.UN_BLOCK_PLAYER), String.valueOf(isUnBlocked));
        serverHandler.out.println(gsonRequest);
    }
}