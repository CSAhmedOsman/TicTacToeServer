/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strategy;

import database.PlayerDAO;
import java.util.ArrayList;
import model.Player;
import server.ServerHandler;
import util.Constants;
import util.JsonHandler;


/**
 *
 * @author w
 */
public class GetAvailableStrategy implements RequestHandlerStrategy {
    @Override
    public void handleRequest(ServerHandler serverHandler) {
        getAvailablePlayers(serverHandler);
    }

    public void getAvailablePlayers(ServerHandler serverHandler) {
        ArrayList<Player> players = PlayerDAO.getAvailablePlayers();
        
        String gsonResponse = JsonHandler.serializeJson(String.valueOf(Constants.GET_AVAILIABLE_PLAYERS),
                JsonHandler.serelizeObject(players));
        serverHandler.out.println(gsonResponse);
    }
}
