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
public class RemoveFriendStrategy implements RequestHandlerStrategy {
    @Override
    public void handleRequest(ServerHandler serverHandler) {
        removeFriend(serverHandler);
    }
    
    private void removeFriend(ServerHandler serverHandler) {
        int friendId = Integer.valueOf(serverHandler.requestData.get(1));

        boolean isNotFriend = PlayerDAO.removeFriend(serverHandler.playerId, friendId);

        String gsonRequest = JsonHandler.serializeJson(String.valueOf(Constants.REMOVE_FRIEND), String.valueOf(isNotFriend));
        serverHandler.out.println(gsonRequest);
    }
}
