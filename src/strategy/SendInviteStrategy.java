/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strategy;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import dto.GameInfo;
import server.ServerHandler;
import util.Constants;
import util.JsonHandler;


/**
 *
 * @author w
 */
public class SendInviteStrategy implements RequestHandlerStrategy {
    @Override
    public void handleRequest(ServerHandler serverHandler) {
        sendInvit(serverHandler);
    }
    
    private void sendInvit(ServerHandler serverHandler) {

        Type gameType = new TypeToken<GameInfo>() {}.getType();
        GameInfo info = JsonHandler.deserializeArray(serverHandler.requestData.get(1), gameType);

        int type = Integer.valueOf(serverHandler.requestData.get(2));
        ServerHandler destinationSocket = serverHandler.getDestinationSocket(info.getDestPlayerId());

        if (destinationSocket != null && !destinationSocket.isInterrupted()) {
            String gsonRequest = JsonHandler.serializeJson(String.valueOf(Constants.SEND_INVITE),
                    JsonHandler.serelizeObject(info), String.valueOf(type));
            destinationSocket.out.println(gsonRequest);
        } else {
            System.err.println("Destination socket is null. Cannot send invitation.");
        }
    }
}
