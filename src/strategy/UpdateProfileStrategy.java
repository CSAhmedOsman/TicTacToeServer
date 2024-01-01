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
import util.JsonHandler;


/**
 *
 * @author w
 */
public class UpdateProfileStrategy implements RequestHandlerStrategy {
    @Override
    public void handleRequest(ServerHandler serverHandler) {
        updateUserProfile(serverHandler);
    }
    
    private void updateUserProfile(ServerHandler serverHandler) {
        Type playerType = new TypeToken<Player>() {}.getType();
        Player currentPlayer = JsonHandler.deserializeArray(serverHandler.requestData.get(1), playerType);

        if (PlayerDAO.updateUserProfile(currentPlayer)) {
            System.out.println("update User Profile");
        }
    }
}