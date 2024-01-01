/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strategy;

import com.google.gson.reflect.TypeToken;
import database.PlayerDAO;
import java.lang.reflect.Type;
import model.GameInfo;
import server.ServerHandler;
import util.JsonHandler;


/**
 *
 * @author w
 */
public class UpdateScoreStrategy implements RequestHandlerStrategy {
    @Override
    public void handleRequest(ServerHandler serverHandler) {
        updateScore(serverHandler);
    }
    
    private void updateScore(ServerHandler serverHandler) {
        Type gameType = new TypeToken<GameInfo>() {}.getType();
        GameInfo info = JsonHandler.deserializeArray(serverHandler.requestData.get(1), gameType);
        int type = Integer.valueOf(serverHandler.requestData.get(2));
        PlayerDAO.updatePlayerScore(info.getSrcPlayerId(), type);
    }
}