/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strategy;

import com.google.gson.reflect.TypeToken;
import database.PlayerDAO;
import java.lang.reflect.Type;
import java.util.ArrayList;
import model.Player;
import server.ServerHandler;
import util.Constants;
import util.JsonHandler;

/**
 *
 * @author w
 */
public class RegisterStrategy implements RequestHandlerStrategy {
    @Override
    public void handleRequest(ServerHandler serverHandler) {
        register(serverHandler);
    }

    public void register(ServerHandler serverHandler) {
        Type playerType = new TypeToken<Player>() {}.getType();
        Player newPlayer = JsonHandler.deserializeArray(serverHandler.requestData.get(1), playerType);
        boolean isRegistered = PlayerDAO.registerPlayer(newPlayer);

        String gsonRequest = JsonHandler.serializeJson(String.valueOf(Constants.REGISTER), Boolean.toString(isRegistered));
        serverHandler.out.println(gsonRequest);
    }
    
    
}
