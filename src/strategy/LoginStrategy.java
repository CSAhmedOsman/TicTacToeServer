/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strategy;

import com.google.gson.reflect.TypeToken;
import database.PlayerDAO;
import java.lang.reflect.Type;
import model.Player;
import server.ServerHandler;
import util.Constants;
import util.JsonHandler;


/**
 *
 * @author w
 */
public class LoginStrategy implements RequestHandlerStrategy {
    @Override
    public void handleRequest(ServerHandler serverHandler) {
        login(serverHandler);
    }

    public void login(ServerHandler serverHandler) {
        Type playerType = new TypeToken<Player>() {}.getType();
        Player currentPlayer = JsonHandler.deserializeArray(serverHandler.requestData.get(1), playerType);

        int authenticatePlayerId = PlayerDAO.authenticatePlayer(currentPlayer);

        if (authenticatePlayerId != Constants.PLAYER_NOT_EXIST) {
            serverHandler.playerId = authenticatePlayerId;
        }

        if (PlayerDAO.isOnline(authenticatePlayerId)) {
            authenticatePlayerId = Constants.PLAYER_ONLINE;
        }

        String gsonResponse = JsonHandler.serializeJson(String.valueOf(Constants.LOGIN), 
                String.valueOf(authenticatePlayerId));
        serverHandler.out.println(gsonResponse);
    }
}