/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strategy;

import database.PlayerDAO;
import model.Player;
import server.ServerHandler;
import util.Constants;
import util.JsonHandler;


/**
 *
 * @author w
 */
public class GetDataStrategy implements RequestHandlerStrategy {
    @Override
    public void handleRequest(ServerHandler serverHandler) {
        getPlayerData(serverHandler);
    }
    
    private void getPlayerData(ServerHandler serverHandler) {
        Player player = PlayerDAO.getDataOfPlayer(serverHandler.playerId);

        String gsonRequest = JsonHandler.serializeJson(String.valueOf(Constants.GET_DATA_OF_PLAYER),
                player.getName(), player.getEmail(), player.getPassword());
        serverHandler.out.println(gsonRequest);
    }
}
