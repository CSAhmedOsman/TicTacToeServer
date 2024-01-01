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
public class AddFriendStrategy implements RequestHandlerStrategy {
    @Override
    public void handleRequest(ServerHandler serverHandler) {
        addFriend(serverHandler);
    }
    
    private void addFriend(ServerHandler serverHandler) {
        int friendId = Integer.valueOf(serverHandler.requestData.get(1));

        boolean isFriend = PlayerDAO.addFriend(serverHandler.playerId, friendId);

        String gsonRequest = JsonHandler.serializeJson(String.valueOf(Constants.ADD_FRIEND), String.valueOf(isFriend));
        serverHandler.out.println(gsonRequest);
    }
}
