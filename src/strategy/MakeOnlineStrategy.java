/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strategy;

import database.PlayerDAO;
import server.ServerHandler;


/**
 *
 * @author w
 */
public class MakeOnlineStrategy implements RequestHandlerStrategy {
    @Override
    public void handleRequest(ServerHandler serverHandler) {
        makePlayerOnline(serverHandler);
    }
    
    private void makePlayerOnline(ServerHandler serverHandler) {
        PlayerDAO.setOnlineStatus(serverHandler.playerId);
    }
}
