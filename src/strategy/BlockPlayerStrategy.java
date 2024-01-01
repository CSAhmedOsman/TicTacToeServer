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
public class BlockPlayerStrategy implements RequestHandlerStrategy {
    @Override
    public void handleRequest(ServerHandler serverHandler) {
        blockPlayer(serverHandler);
    }
    
    private void blockPlayer(ServerHandler serverHandler) {
        int blockedId = Integer.valueOf(serverHandler.requestData.get(1));

        PlayerDAO.removeFriend(serverHandler.playerId, blockedId);
        boolean isBlocked = PlayerDAO.blockPlayer(serverHandler.playerId, blockedId);

        String gsonRequest = JsonHandler.serializeJson(String.valueOf(Constants.BLOCK_PLAYER), String.valueOf(isBlocked));
        serverHandler.out.println(gsonRequest);
    }
}
