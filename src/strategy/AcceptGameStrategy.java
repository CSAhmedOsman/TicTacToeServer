/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strategy;

import database.PlayerDAO;
import model.GameInfo;
import server.ServerHandler;
import util.Constants;
import util.JsonHandler;


/**
 *
 * @author w
 */
public class AcceptGameStrategy implements RequestHandlerStrategy {
    @Override
    public void handleRequest(ServerHandler serverHandler) {
        acceptGame(serverHandler);
    }
    
    private void acceptGame(ServerHandler serverHandler) {
        int destPlayerId = Integer.valueOf(serverHandler.requestData.get(1));
        int type = Integer.valueOf(serverHandler.requestData.get(2));

        PlayerDAO.setNotAvailable(serverHandler.playerId);
        PlayerDAO.setNotAvailable(destPlayerId);

        ServerHandler sourceSocket = serverHandler.getDestinationSocket(serverHandler.playerId);
        ServerHandler destinationSocket = serverHandler.getDestinationSocket(destPlayerId);

        String player1Name = PlayerDAO.getPlayerName(serverHandler.playerId);
        String player2Name = PlayerDAO.getPlayerName(destPlayerId);

        int player1Score = PlayerDAO.getPlayerScore(serverHandler.playerId);
        int player2Score = PlayerDAO.getPlayerScore(destPlayerId);

        GameInfo info = new GameInfo(player2Name, player1Name, destPlayerId, serverHandler.playerId, player2Score, player1Score);

        String gsonRequest = JsonHandler.serializeJson(String.valueOf(Constants.ACCEPT_GAME),
                JsonHandler.serelizeObject(info), String.valueOf(true), String.valueOf(type));
        destinationSocket.out.println(gsonRequest);

        info = new GameInfo(player1Name, player2Name, serverHandler.playerId, destPlayerId, player1Score, player2Score);

        gsonRequest = JsonHandler.serializeJson(String.valueOf(Constants.ACCEPT_GAME),
                JsonHandler.serelizeObject(info), String.valueOf(false), String.valueOf(type));
        sourceSocket.out.println(gsonRequest);
    }
}
